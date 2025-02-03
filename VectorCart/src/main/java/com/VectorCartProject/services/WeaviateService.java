package com.VectorCartProject.services;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.graphql.query.fields.GenerativeSearchBuilder;

import java.util.HashMap;
import java.util.Map;

public class WeaviateService {
    private static WeaviateService instance;
    final private WeaviateClient client;

    // Private constructor to prevent instantiation
    private WeaviateService() {
        String scheme = "http";
        String host = "localhost:8080";
        String openaiApiKey = System.getenv("OPENAI_API_KEY");

        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("X-OpenAI-Api-Key", openaiApiKey);
            }
        };

        Config config = new Config(scheme, host, headers);
        client = new WeaviateClient(config);
    }

    // Public method to get the singleton instance
    public static synchronized WeaviateService getInstance() {
        if (instance == null) {
            instance = new WeaviateService();
        }
        return instance;
    }

    public WeaviateClient getClient() {
        return client;
    }

    public boolean testConnection() {
        Result<Boolean> result = client.misc().liveChecker().run();
        return result.getResult();
    }

    public Result<GraphQLResponse> nearTextSearch(String className, String searchQuery) {
        NearTextArgument nearText = NearTextArgument.builder()
                .concepts(new String[]{searchQuery})
                .build();

        Fields fields = Fields.builder()
                .fields(new Field[]{
                        Field.builder().name("name").build(),
                        Field.builder().name("productId").build(),
                        Field.builder().name("_additional").fields(new Field[]{
                                Field.builder().name("distance").build()
                        }).build()
                })
                .build();

        String query = GetBuilder.builder()
                .className(className)
                .fields(fields)
                .withNearTextFilter(nearText)
                .limit(2)
                .build()
                .buildQuery();

        return client.graphQL().raw().withQuery(query).run();
    }

    public Result<GraphQLResponse> generativeSearch(String className, String searchQuery, String ragQuery) {
        NearTextArgument nearText = NearTextArgument.builder()
                .concepts(new String[]{searchQuery})
                .build();

        String[] properties = {"name", "description"};
        GenerativeSearchBuilder ragSearch = GenerativeSearchBuilder.builder()
                .groupedResultTask(ragQuery)
                .groupedResultProperties(properties)
                .build();

        Fields fields = Fields.builder()
                .fields(new Field[]{
                        Field.builder().name("name").build(),
                        Field.builder().name("productId").build(),
                })
                .build();

        String query = GetBuilder.builder()
                .className(className)
                .fields(fields)
                .withNearTextFilter(nearText)
                .withGenerativeSearch(ragSearch)
                .limit(2)
                .build()
                .buildQuery();

        return client.graphQL().raw().withQuery(query).run();
    }
}
