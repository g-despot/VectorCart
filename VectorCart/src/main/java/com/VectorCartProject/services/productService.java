package com.VectorCartProject.services;

import java.util.ArrayList;
import java.util.List;

import com.VectorCartProject.models.Category;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VectorCartProject.dao.productDao;
import com.VectorCartProject.dao.categoryDao;
import com.VectorCartProject.models.Product;

@Service
public class productService {
    @Autowired
    private productDao productDao;
    @Autowired
    private categoryDao categoryDao;

    @Autowired
    public productService(productDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getProducts() {
        return this.productDao.getProducts();
    }

    public Product addProduct(Product product) {
        return this.productDao.addProduct(product);
    }

    public Product getProduct(int id) {
        return this.productDao.getProduct(id);
    }

    public Product updateProduct(int id, Product product) {
        product.setId(id);
        return this.productDao.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        return this.productDao.deletProduct(id);
    }

    public List<Product> getProductsFromResult(Result<GraphQLResponse> result) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> productList = new ArrayList<>();

        try {
            GraphQLResponse graphQLResponse = result.getResult();
            JsonNode productsNode = objectMapper.valueToTree(graphQLResponse.getData());
            JsonNode productsArrayNode = productsNode.at("/Get/Products");
            if (productsArrayNode.isArray()) {
                for (JsonNode productNode : productsArrayNode) {
                    int productId = productNode.get("productId").asInt();
                    productList.add(this.productDao.getProduct(productId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public String getRagGroupedFromResult(Result<GraphQLResponse> result) {
        ObjectMapper objectMapper = new ObjectMapper();
        String groupedResult = "";

        try {
            GraphQLResponse graphQLResponse = result.getResult();
            JsonNode productsNode = objectMapper.valueToTree(graphQLResponse.getData());
            JsonNode generativeNode = productsNode.at("/Get/Products/0/_additional/generate");

            groupedResult = generativeNode.get("groupedResult").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupedResult;
    }
}
