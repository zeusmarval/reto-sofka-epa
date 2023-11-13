package com.zama.reto_sofka.service;

import com.zama.reto_sofka.model.ItemSales;
import com.zama.reto_sofka.model.Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SaleService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Sales registerSales(Sales sales) {
        return mongoTemplate.save(sales, "Sales");
    }


    public void calculateTotalInvoices() {
        List<Sales> _sales = mongoTemplate.findAll(Sales.class, "Sales");

        for (Sales sales : _sales) {
            double totalValue = 0.0;

            for (ItemSales item : sales.getItems()) {
                totalValue += item.getPrice() * item.getQuantity();
            }

            sales.setTotalValue(totalValue);
            mongoTemplate.save(sales, "Sales");
        }
    }

    public List<Sales> getPaginatedInvoices(int page, int size) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Query query = new Query().with(pageable);
        return mongoTemplate.find(query, Sales.class, "Sales");
    }

}
