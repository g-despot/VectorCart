package com.VectorCartProject.services.weaviate;

import com.VectorCartProject.models.Product;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Common interface for Weaviate operations regardless of client version
 */
public interface WeaviateService {
    /**
     * Test if Weaviate is reachable
     * @return true if connection is successful
     */
    boolean testConnection();

    /**
     * Get client version
     * @return "v5" or "v6" depending on implementation
     */
    String getVersion();

    /**
     * Delete a collection
     * @param className name of the collection to delete
     * @throws IOException if an error occurs
     */
    void deleteCollection(String className) throws IOException;

    /**
     * Create a new collection with properties for products
     * @param className name of the collection to create
     * @throws IOException if an error occurs
     */
    void createCollection(String className) throws IOException;

    /**
     * Populate a collection with products
     * @param className name of the collection
     * @param products list of products to add
     * @throws IOException if an error occurs
     */
    void populateCollection(String className, List<Product> products) throws IOException;

    /**
     * Perform a search using text
     * @param className name of the collection to search
     * @param searchQuery the text query
     * @return search results
     */
    Object nearTextSearch(String className, String searchQuery);

    /**
     * Perform a generative search with RAG
     * @param className name of the collection to search
     * @param searchQuery the search query
     * @param ragQuery the generative query
     * @return search results
     */
    Object generativeSearch(String className, String searchQuery, String ragQuery);

    /**
     * Perform a vector search
     * @param className name of the collection to search
     * @param vector the vector to search near
     * @return search results
     */
    Object nearVectorSearch(String className, Float[] vector);
}