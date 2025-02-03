package com.VectorCartProject;

import com.VectorCartProject.models.Product;
import com.VectorCartProject.services.productService;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.schema.model.DataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

        client = WeaviateHelper.getInstance().getClient();

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
        //this.deleteCollection();
        //this.createCollection();
        //this.populateWeaviate();
    }

    public void deleteCollection(){
        Result<Boolean> result = client.schema().classDeleter()
                .withClassName(className)
                .run();
    }

    public void createCollection() {

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

        WeaviateClass emptyClass = WeaviateClass.builder()
                .className(className)
                .properties(Arrays.asList(idProperty, nameProperty, descriptionProperty))
                .vectorizer("text2vec-openai")
                .build();

        Result<Boolean> result = client.schema().classCreator()
                .withClass(emptyClass)
                .run();
    }

    public void populateWeaviate() {
        List<Product> products = this.productService.getProducts();
        List<Result<WeaviateObject>> results = new ArrayList<>();
        for (Product product : products) {
            System.out.println("Product: " + product.getName());
            results.add(client.data().creator()
                    .withClassName(className)
                    .withProperties(new HashMap<String, Object>() {{
                        put("productId", product.getId());
                        put("name", product.getName());
                        put("description", product.getDescription());
                    }})
                    .run());
        }
        /*for(Result<WeaviateObject> result:results){
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(result.getResult());
            System.out.println(json);
        }*/
    }
}
