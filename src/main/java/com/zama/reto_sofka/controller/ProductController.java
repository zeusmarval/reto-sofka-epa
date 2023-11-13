package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Product;
import com.zama.reto_sofka.model.SoldProducts;
import com.zama.reto_sofka.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProductController(MongoTemplate mongoTemplate, ProductService productService) {
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
    }

    @GetMapping("/most-selled-products")
    public List<SoldProducts> mostSelledProducts(@RequestParam int limit, @RequestParam String startDate, @RequestParam String endDate) {
        return productService.mostSelledProducts(limit, startDate, endDate);
    }

    @GetMapping("/all-selled")
    public List<Product> allSelled() {
        return ProductService.allSelled(mongoTemplate);
    }
}
