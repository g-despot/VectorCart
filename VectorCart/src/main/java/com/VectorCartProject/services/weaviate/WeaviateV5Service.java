package com.VectorCartProject.services.weaviate;

import com.VectorCartProject.models.Product;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;
import io.weaviate.client.v1.graphql.query.fields.GenerativeSearchBuilder;
import io.weaviate.client.v1.misc.model.Meta;
import io.weaviate.client.v1.schema.model.DataType;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.v1.schema.model.WeaviateClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Weaviate service implementation using V5 client
 */
public class WeaviateV5Service implements WeaviateService {
    private static WeaviateV5Service instance;
    private final WeaviateClient client;

    private WeaviateV5Service() {
        String scheme = "http";
        String host = "localhost:8080";
        String openaiApiKey = System.getenv("OPENAI_API_KEY");

        Map<String, String> headers = new HashMap<String, String>() {{
            put("X-OpenAI-Api-Key", openaiApiKey);
        }};

        Config config = new Config(scheme, host, headers);
        client = new WeaviateClient(config);
    }

    public static synchronized WeaviateV5Service getInstance() {
        if (instance == null) {
            instance = new WeaviateV5Service();
        }
        return instance;
    }

    public WeaviateClient getClient() {
        return client;
    }

    @Override
    public String getVersion() {
        return "v5";
    }

    @Override
    public boolean testConnection() {
        Result<Boolean> result = client.misc().liveChecker().run();
        return result.getResult();
    }

    public Meta getMetaInfo() {
        Result<Meta> meta = client.misc().metaGetter().run();
        if (meta.getError() == null) {
            return meta.getResult();
        }
        return null;
    }

    @Override
    public void deleteCollection(String className) {
        client.schema().classDeleter()
                .withClassName(className)
                .run();
    }

    @Override
    public void createCollection(String className) {
        Property idProperty = Property.builder()
                .name("productId")
                .description("ID of the product")
                .dataType(Arrays.asList(DataType.NUMBER))
                .build();
        Property nameProperty = Property.builder()
                .name("name")
                .description("Name of the product")
                .dataType(Arrays.asList(DataType.TEXT))
                .build();
        Property descriptionProperty = Property.builder()
                .name("description")
                .description("Description of the product")
                .dataType(Arrays.asList(DataType.TEXT))
                .build();
        Property imageUrlProperty = Property.builder()
                .name("imageUrl")
                .description("Image URL of the product")
                .dataType(Arrays.asList(DataType.TEXT))
                .build();
        Property imageProperty = Property.builder()
                .name("image")
                .description("Image of the product")
                .dataType(Arrays.asList(DataType.BLOB))
                .build();
        Property priceProperty = Property.builder()
                .name("price")
                .description("Price of the product in USD")
                .dataType(Arrays.asList(DataType.NUMBER))
                .build();
        Property quantityProperty = Property.builder()
                .name("quantity")
                .description("Quantity of the product available")
                .dataType(Arrays.asList(DataType.INT))
                .build();

        Map<String, Object> generativeOpenAiSettings = new HashMap<>();
        generativeOpenAiSettings.put("model", "gpt-4");
        generativeOpenAiSettings.put("maxTokensProperty", "500");

        Map<String, Map<String, Object>> moduleConfig = new HashMap<>();
        moduleConfig.put("generative-openai", generativeOpenAiSettings);

        WeaviateClass emptyClass = WeaviateClass.builder()
                .className(className)
                .properties(Arrays.asList(
                        idProperty, nameProperty, descriptionProperty, imageUrlProperty, imageProperty, priceProperty,
                        quantityProperty))
                .moduleConfig(moduleConfig)
                .vectorizer("text2vec-openai")
                .build();

        client.schema().classCreator()
                .withClass(emptyClass)
                .run();
    }

    @Override
    public void populateCollection(String className, List<Product> products) throws IOException {
        List<Result<WeaviateObject>> results = new ArrayList<>();
        for (Product product : products) {
            System.out.println("Product: " + product.getName());
            results.add(client.data().creator()
                    .withClassName(className)
                    .withProperties(new HashMap<String, Object>() {{
                        put("productId", product.getId());
                        put("name", product.getName());
                        put("description", product.getDescription());
                        put("imageUrl", product.getImage());
                        put("image", encodeImageToBase64(product.getImage()));
                        put("price", product.getPrice());
                        put("quantity", product.getQuantity());
                    }})
                    .run());
        }
    }

    @Override
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

    @Override
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

    @Override
    public Object nearVectorSearch(String className, Float[] vector) {
        // V5 doesn't have direct vector search implementation in this code
        // This could be implemented following Weaviate v5 approach if needed
        throw new UnsupportedOperationException("nearVectorSearch not implemented for V5 client");
    }

    private static String encodeImageToBase64(String imageUrl) {
        try {
            // Read image from URL
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            // Convert image to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            // Encode to Base64
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}