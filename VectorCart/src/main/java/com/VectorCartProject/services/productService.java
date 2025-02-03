package com.VectorCartProject.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VectorCartProject.dao.productDao;
import com.VectorCartProject.models.Product;

@Service
public class productService {
	@Autowired
	private productDao productDao;
	
	public List<Product> getProducts(){
		return this.productDao.getProducts();
	}
	
	public Product addProduct(Product product) {
		return this.productDao.addProduct(product);
	}
	
	public Product getProduct(int id) {
		return this.productDao.getProduct(id);
	}

	public Product updateProduct(int id,Product product){
		product.setId(id);
		return this.productDao.updateProduct(product);
	}
	public boolean deleteProduct(int id) {
		return this.productDao.deletProduct(id);
	}

	public List<Product> parseGraphQLResponse(Result<GraphQLResponse> result) {
		ObjectMapper objectMapper = new ObjectMapper();

		List<Product> productList = new ArrayList<>();

		try {
			GraphQLResponse graphQLResponse = result.getResult();
// Convert the result to JsonNode if it's an object
			JsonNode productsNode = objectMapper.valueToTree(graphQLResponse.getData());  // Converts Object to JsonNode

			// Get the "Products" from GraphQLResponse (parse as JSON)
			JsonNode productsArrayNode = productsNode.at("/Get/Products");
			if (productsArrayNode.isArray()) {
				for (JsonNode productNode : productsArrayNode) {
					// Extract name and description directly from the JSON node
					String name = productNode.get("name").asText();
					String description = productNode.get("description").asText();

					// Create and populate Product object
					Product product = new Product();
					product.setName(name);
					product.setDescription(description);

					productList.add(product);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}
}
