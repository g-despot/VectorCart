package com.VectorCartProject.services.weaviate;

import com.VectorCartProject.models.Product;
import io.weaviate.client6.Config;
import io.weaviate.client6.WeaviateClient;
import io.weaviate.client6.v1.Vectors;
import io.weaviate.client6.v1.collections.VectorIndex;
import io.weaviate.client6.v1.collections.Vectorizer;
import io.weaviate.client6.v1.collections.Property;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Weaviate service implementation using V6 client
 */
public class WeaviateV6Service implements WeaviateService {
    private static WeaviateV6Service instance;
    private final WeaviateClient client;

    private WeaviateV6Service() {
        String scheme = "http";
        String httpHost = "localhost:8080";
        String grpcHost = "localhost:50051";

        Config config = new Config(scheme, httpHost, grpcHost);
        client = new WeaviateClient(config);
    }

    public static synchronized WeaviateV6Service getInstance() {
        if (instance == null) {
            instance = new WeaviateV6Service();
        }
        return instance;
    }

    public WeaviateClient getClient() {
        return client;
    }

    @Override
    public String getVersion() {
        return "v6";
    }

    @Override
    public boolean testConnection() {
//        try {
//            // Use any basic operation to test connection
//            client.metadata.getLiveStatus();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
        return true;
    }

    @Override
    public void deleteCollection(String className) throws IOException {
        client.collections.delete(className);
    }

    @Override
    public void createCollection(String className) throws IOException {
        client.collections.create(className,
                collection -> collection.properties(
                                Property.integer("productId"),
                                Property.text("name"),
                                Property.text("description"),
                                Property.text("imageUrl"),
                                Property.integer("price"),
                                Property.integer("quantity"))
                        .vector(new VectorIndex<>(VectorIndex.IndexingStrategy.hnsw(), Vectorizer.none())));
    }

    @Override
    public void populateCollection(String className, List<Product> products) throws IOException {
        var productsCollection = client.collections.use(className);
        for (Product product : products) {
            productsCollection.data.insert(new HashMap<String, Object>() {{
                put("productId", product.getId());
                put("name", product.getName());
                put("description", product.getDescription());
                put("imageUrl", product.getImage());
                put("price", product.getPrice());
                put("quantity", product.getQuantity());
            }}, metadata -> metadata.vectors(Vectors.of(new Float[]{1f})));
        }
    }

    @Override
    public Object nearTextSearch(String className, String searchQuery) {
        // This would need to be implemented based on v6 approach
        // For compatibility, you might want to transform the result to match v5's structure
        throw new UnsupportedOperationException("nearTextSearch not implemented for V6 client");
    }

    @Override
    public Object generativeSearch(String className, String searchQuery, String ragQuery) {
        // This would need to be implemented based on v6 approach
        // For compatibility, you might want to transform the result to match v5's structure
        throw new UnsupportedOperationException("generativeSearch not implemented for V6 client");
    }

    @Override
    public Object nearVectorSearch(String className, Float[] vector) {
        var productsCollection = client.collections.use(className);
        var response = productsCollection.query.nearVector(vector, o -> o.distance(0.1f));
        return response.objects.get(0).properties;
    }
}