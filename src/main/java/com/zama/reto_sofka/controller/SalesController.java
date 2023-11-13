package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Sales;
import com.zama.reto_sofka.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SaleService saleService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SalesController(MongoTemplate mongoTemplate, SaleService saleService) {
        this.mongoTemplate = mongoTemplate;
        this.saleService = saleService;
    }

    @PostMapping
    public Sales registerSales(@RequestBody Sales sales) {
        sales.setSaleDate(new Date());
        return saleService.registerSales(sales);
    }

    @GetMapping("/get-invoices")
    public List<Sales> getPaginatedInvoices(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "100") int size) {
        return saleService.getPaginatedInvoices(page, size);
    }

}
