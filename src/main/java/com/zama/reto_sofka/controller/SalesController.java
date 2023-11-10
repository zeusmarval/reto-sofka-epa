package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Product;
import com.zama.reto_sofka.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SalesController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
