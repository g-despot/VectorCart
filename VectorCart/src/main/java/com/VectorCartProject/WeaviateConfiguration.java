package com.VectorCartProject;

import com.VectorCartProject.models.Product;
import com.VectorCartProject.services.productService;
import com.VectorCartProject.services.userService;
import com.google.gson.GsonBuilder;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.schema.model.DataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.misc.model.Meta;

@Service
public class WeaviateConfiguration implements CommandLineRunner {

    @Autowired
    private final productService productService;
    private final WeaviateClient client;
    private final String className = "Products";


    @Autowired
    public WeaviateConfiguration() {
        this.productService = new productService();
        Config config = new Config("http", "localhost:8080");
        client = new WeaviateClient(config);
        Result<Meta> meta = client.misc().metaGetter().run();
        if (meta.getError() == null) {
            System.out.printf("meta.hostname: %s\n", meta.getResult().getHostname());
            System.out.printf("meta.version: %s\n", meta.getResult().getVersion());
            System.out.printf("meta.modules: %s\n", meta.getResult().getModules());
        } else {
            System.out.printf("Error: %s\n", meta.getError().getMessages());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.createCollection();
        this.populateWeaviate();
    }

    public boolean createCollection() {

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

        WeaviateClass emptyClass = WeaviateClass.builder()
                .className(className)
                .properties(Arrays.asList(nameProperty, descriptionProperty))
                .vectorizer("text2vec-openai")
                .build();

        Result<Boolean> result = client.schema().classCreator()
                .withClass(emptyClass)
                .run();
        return true;
    }

    public boolean populateWeaviate() {
        List<Product> products = this.productService.getProducts();
        List<Result<WeaviateObject>> results = new ArrayList<>();
        for (Product product : products) {
            results.add(client.data().creator()
                    .withClassName(className)
                    .withProperties(new HashMap<String, Object>() {{
                        put("name", product.getName());
                        put("description", product.getDescription());
                    }})
                    .run());
        }
        for(Result<WeaviateObject> result:results){
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(result.getResult());
            System.out.println(json);
        }
        return true;
    }
}
