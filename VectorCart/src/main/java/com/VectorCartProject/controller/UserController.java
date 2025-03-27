package com.VectorCartProject.controller;

import com.VectorCartProject.models.Product;
import com.VectorCartProject.models.User;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.VectorCartProject.services.weaviate.WeaviateService;
import com.VectorCartProject.services.weaviate.WeaviateServiceFactory;
import com.VectorCartProject.services.weaviate.WeaviateServiceFactory.ClientVersion;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.VectorCartProject.services.userService;
import com.VectorCartProject.services.productService;

import io.weaviate.client.base.Result;

@Controller
public class UserController {

    private final userService userService;
    private final productService productService;
    private final WeaviateService weaviateV5Service;
    private final WeaviateService weaviateV6Service;

    @Autowired
    public UserController(userService userService, productService productService) {
        this.userService = userService;
        this.productService = productService;
        this.weaviateV5Service = WeaviateServiceFactory.getService(ClientVersion.V5);
        this.weaviateV6Service = WeaviateServiceFactory.getService(ClientVersion.V6);
    }

    @GetMapping("/register")
    public String registerUser() {
        return "register";
    }

    @GetMapping("/login")
    public ModelAndView userLogin(@RequestParam(required = false) String error) {
        ModelAndView mv = new ModelAndView("userLogin");
        if ("true".equals(error)) {
            mv.addObject("msg", "Please enter correct email and password");
        }
        return mv;
    }

    @GetMapping("/")
    public ModelAndView indexPage() {
        ModelAndView mView = new ModelAndView("index");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        mView.addObject("username", username);
        List<Product> products = this.productService.getProducts();

        if (products.isEmpty()) {
            mView.addObject("msg", "No products are available");
        } else {
            mView.addObject("products", products);
        }
        return mView;
    }

    @RequestMapping(value = "registerUser", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute User user) {
        // Check if username already exists in database
        boolean exists = this.userService.checkUserExists(user.getUsername());

        if (!exists) {
            System.out.println(user.getEmail());
            user.setRole("ROLE_NORMAL");
            this.userService.addUser(user);

            System.out.println("New user created: " + user.getUsername());
            return new ModelAndView("userLogin");
        } else {
            System.out.println("New user not created - username taken: " + user.getUsername());
            ModelAndView mView = new ModelAndView("register");
            mView.addObject("msg", user.getUsername() + " is taken. Please choose a different username.");
            return mView;
        }
    }

    @GetMapping("/profile")
    public String getProfile(Model model, HttpServletRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        if (user != null) {
            model.addAttribute("userid", user.getId());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("password", user.getPassword());
            model.addAttribute("address", user.getAddress());
        } else {
            model.addAttribute("msg", "User not found");
        }

        return "updateProfile";
    }

    @GetMapping("search")
    public ModelAndView getSearchWithQuery(@RequestParam(value = "searchQuery", required = false) String searchQuery) {
        ModelAndView mView = new ModelAndView("search");
        List<Product> products;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            System.out.println("searchQuery: " + searchQuery);
            Result<GraphQLResponse> results = (Result<GraphQLResponse>) weaviateV5Service.nearTextSearch("Products", searchQuery);
            System.out.println("Raw response: " + results);
            products = productService.getProductsFromResult(results);

            for (Product product : products) {
                System.out.println(product.getName() + " - " + product.getDescription());
            }
        } else {
            products = this.productService.getProducts();
        }

        if (products.isEmpty()) {
            mView.addObject("msg", "No products are available");
        } else {
            mView.addObject("products", products);
        }
        return mView;
    }

    @GetMapping("rag")
    public ModelAndView getRag(@RequestParam(value = "searchQuery", required = false) String searchQuery,
                               @RequestParam(value = "ragQuery", required = false) String ragQuery) {
        ModelAndView mView = new ModelAndView("rag");
        List<Product> products;
        String generativeResult = "";

        if (searchQuery != null && !searchQuery.isEmpty()) {
            System.out.println("searchQuery: " + searchQuery);

            Result<GraphQLResponse> results = (Result<GraphQLResponse>) weaviateV5Service.generativeSearch("Products", searchQuery,
                    ragQuery);
            System.out.println("Raw response: " + results);

            generativeResult = productService.getRagGroupedFromResult(results);
            System.out.println("generativeResult: " + generativeResult);

            products = productService.getProductsFromResult(results);
            for (Product product : products) {
                System.out.println(product.getName() + " - " + product.getDescription());
            }
        } else {
            products = this.productService.getProducts();
        }

        if (products.isEmpty() || generativeResult.isEmpty()) {
            mView.addObject("msg", "No products are available");
        } else {
            mView.addObject("products", products);
            mView.addObject("generativeGroupedResult", generativeResult);
        }
        return mView;
    }
}
