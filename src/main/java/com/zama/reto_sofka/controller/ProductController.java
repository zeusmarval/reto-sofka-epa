package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Product;
import com.zama.reto_sofka.model.SoldProducts;
import com.zama.reto_sofka.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    @Operation(summary = "Obtiene los productos más vendidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "504", description = "Conflict",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "El límite debe ser mayor o igual a 1 y menor o igual a 10"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Error interno del servidor:\n" +
                                    "Required request parameter 'limit' for method parameter type int is not present")))
    })
    public Collection<SoldProducts> mostSelledProducts(@RequestParam int limit,
                                                       @RequestParam String startDate,
                                                       @RequestParam String endDate) {
        if (limit < 1) {
            throw new IllegalArgumentException("El límite debe ser mayor o igual a 1");
        }
        if (limit > 10) {
            throw new IllegalArgumentException("El límite debe ser menor o igual a 10");
        }

        if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}") || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("El formato de fecha debe ser yyyy-MM-dd");
        }

        return ResponseEntity.ok(productService.mostSelledProducts(limit, startDate, endDate)).getBody();
    }

    @GetMapping("/all-selled")
    @Operation(summary = "Obtiene todos los productos vendidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public List<Product> allSelled() {
        return ProductService.allSelled(mongoTemplate);
    }

}
