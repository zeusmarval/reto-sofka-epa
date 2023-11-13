package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Product;
import com.zama.reto_sofka.model.SoldProducts;
import com.zama.reto_sofka.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
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
    public Collection<SoldProducts> mostSelledProducts(@RequestParam int limit,
                                                       @RequestParam String startDate,
                                                       @RequestParam String endDate) {
        List<String> errors = new ArrayList<>();
        // Validación del límite
        if (limit < 1) {
            throw new IllegalArgumentException("El límite debe ser mayor o igual a 1");
        }

        // Validación del formato de fecha
        if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}") || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("El formato de fecha debe ser yyyy-MM-dd");
        }

        return ResponseEntity.ok(productService.mostSelledProducts(limit, startDate, endDate)).getBody();
    }

    @GetMapping("/all-selled")
    public List<Product> allSelled() {
        return ProductService.allSelled(mongoTemplate);
    }
}
