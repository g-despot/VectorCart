package com.VectorCartProject.controller;

import com.VectorCartProject.WeaviateConfiguration;
import com.VectorCartProject.WeaviateSingleton;
import com.VectorCartProject.models.Product;
import com.VectorCartProject.models.User;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.VectorCartProject.services.userService;
import com.VectorCartProject.services.productService;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.misc.model.Meta;

@Controller
public class UserController {

    private final userService userService;
    private final productService productService;

    @Autowired
    public UserController(userService userService, productService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/register")
    public String registerUser() {
        return "register";
    }

    @GetMapping("/buy")
    public String buy() {
        return "buy";
    }

    @GetMapping("/login")
    public ModelAndView userlogin(@RequestParam(required = false) String error) {
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

    @GetMapping("/user/products")
    public ModelAndView getproduct() {

        ModelAndView mView = new ModelAndView("uproduct");

        List<Product> products = this.productService.getProducts();

        if (products.isEmpty()) {
            mView.addObject("msg", "No products are available");
        } else {
            mView.addObject("products", products);
        }

        return mView;
    }

    @RequestMapping(value = "newuserregister", method = RequestMethod.POST)
    public ModelAndView newUseRegister(@ModelAttribute User user) {
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

    @GetMapping("/profileDisplay")
    public String profileDisplay(Model model, HttpServletRequest request) {

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


    //for Learning purpose of model
    @GetMapping("/test")
    public String Test(Model model) {
        Config config = new Config("http", "localhost:8080");
        WeaviateClient client = new WeaviateClient(config);
        Result<Meta> meta = client.misc().metaGetter().run();
        if (meta.getError() == null) {
            System.out.printf("meta.hostname: %s\n", meta.getResult().getHostname());
            System.out.printf("meta.version: %s\n", meta.getResult().getVersion());
            System.out.printf("meta.modules: %s\n", meta.getResult().getModules());
        } else {
            System.out.printf("Error: %s\n", meta.getError().getMessages());
        }

        System.out.println("test page");
        model.addAttribute("author", "jay gajera");
        model.addAttribute("id", 40);

        List<String> friends = new ArrayList<String>();
        model.addAttribute("f", friends);
        friends.add("xyz");
        friends.add("abc");

        return "test";
    }

    // for learning purpose of model and view ( how data is pass to view)

    @GetMapping("/test2")
    public ModelAndView Test2() {
        System.out.println("test page");
        //create modelandview object
        ModelAndView mv = new ModelAndView();
        mv.addObject("name", "jay gajera 17");
        mv.addObject("id", 40);
        mv.setViewName("test2");

        List<Integer> list = new ArrayList<Integer>();
        list.add(10);
        list.add(25);
        mv.addObject("marks", list);
        return mv;
    }

    @GetMapping("search")
    public ModelAndView getSearchWithQuery(@RequestParam("searchQuery") String searchQuery) {
        ModelAndView mView = new ModelAndView("search");
        if(!searchQuery.isEmpty()) {
            System.out.println("searchQuery: " + searchQuery);
            WeaviateClient client = WeaviateSingleton.getInstance().getClient();

            Result<GraphQLResponse> results = WeaviateSingleton.getInstance().nearTextSearch("Products", searchQuery);
            System.out.println(results);
        }
        List<Product> products = this.productService.getProducts();

        if (products.isEmpty()) {
            mView.addObject("msg", "No products are available");
        } else {
            mView.addObject("products", products);
        }
        return mView;
    }
}