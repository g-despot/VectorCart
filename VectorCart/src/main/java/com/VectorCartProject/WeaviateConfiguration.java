package com.VectorCartProject;

import com.VectorCartProject.models.Product;
import com.VectorCartProject.services.productService;
import com.VectorCartProject.services.weaviate.WeaviateService;
import com.VectorCartProject.services.weaviate.WeaviateServiceFactory;
import com.VectorCartProject.services.weaviate.WeaviateServiceFactory.ClientVersion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class WeaviateConfiguration implements CommandLineRunner {

    private final productService productService;
    private final WeaviateService weaviateV5Service;
    private final WeaviateService weaviateV6Service;
    private final String className = "Products";

    @Autowired
    public WeaviateConfiguration(productService productService) {
        this.productService = productService;

        // Get both service instances
        this.weaviateV5Service = WeaviateServiceFactory.getService(ClientVersion.V5);
        this.weaviateV6Service = WeaviateServiceFactory.getService(ClientVersion.V6);

        // Test connection to Weaviate
        if (weaviateV5Service.testConnection()) {
            System.out.println("Connected to Weaviate using V5 client");
        } else {
            System.out.println("Failed to connect to Weaviate using V5 client");
        }
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Example of using V5 for some operations and V5 for others
            setupCollectionWithV5();

            // Example of performing search with V5
            // searchWithV5("laptop");

            // Example of performing vector search with V6
            // Float[] vector = new Float[]{0.1f, 0.2f, 0.3f};
            // searchVectorWithV6(vector);
        } catch (Exception e) {
            System.out.println("Error during Weaviate initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set up collection using V6 client
     */
    private void setupCollectionWithV6() throws IOException {
        System.out.println("Setting up collection using V6 client");
        weaviateV6Service.deleteCollection(className);
        weaviateV6Service.createCollection(className);
        weaviateV6Service.populateCollection(className, productService.getProducts());
    }

    /**
     * Set up collection using V5 client
     */
    private void setupCollectionWithV5() throws IOException {
        System.out.println("Setting up collection using V5 client");
        weaviateV5Service.deleteCollection(className);
        weaviateV5Service.createCollection(className);
        weaviateV5Service.populateCollection(className, productService.getProducts());
    }

    /**
     * Search using V5 client
     */
    private void searchWithV5(String query) {
        System.out.println("Searching with V5 client: " + query);
        Object results = weaviateV5Service.nearTextSearch(className, query);
        System.out.println("Search results: " + results);
    }

    /**
     * RAG search using V5 client
     */
    private void ragSearchWithV5(String query, String ragPrompt) {
        System.out.println("RAG search with V5 client: " + query);
        Object results = weaviateV5Service.generativeSearch(className, query, ragPrompt);
        System.out.println("RAG results: " + results);
    }

    /**
     * Vector search using V6 client
     */
    private void searchVectorWithV6(Float[] vector) {
        System.out.println("Vector search with V6 client");
        Object results = weaviateV6Service.nearVectorSearch(className, vector);
        System.out.println("Vector search results: " + results);
    }

    /**
     * Sample of using both clients together
     */
    private void hybridOperation() throws IOException {
        // Set up with V6
        weaviateV6Service.deleteCollection(className);
        weaviateV6Service.createCollection(className);

        // Populate with V5
        List<Product> products = productService.getProducts();
        weaviateV5Service.populateCollection(className, products);

        // Search with V5
        searchWithV5("laptop");
    }
}