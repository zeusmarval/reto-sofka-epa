package com.zama.reto_sofka.service;

import com.zama.reto_sofka.model.ItemSales;
import com.zama.reto_sofka.model.Sales;
import com.zama.reto_sofka.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class SaleService implements Runnable {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SaleService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Sales registerSales(Sales sales) {
        return mongoTemplate.save(sales, "Sales");
    }

    public void generateProducts() {
        List<Sales> _sales = mongoTemplate.findAll(Sales.class, "Sales");

        Map<String, Integer> productsByName = new HashMap<>();

        for (Sales sale : _sales) {
            for (ItemSales item : sale.getItems()) {
                String productName = item.getName();
                int quantitySold = item.getQuantity();

                if (productsByName.containsKey(productName)) {
                    int currentQuantity = productsByName.get(productName);
                    productsByName.put(productName, currentQuantity + quantitySold);
                } else {
                    productsByName.put(productName, quantitySold);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : productsByName.entrySet()) {
            String productName = entry.getKey();
            int quantitySold = entry.getValue();

            Product product = mongoTemplate.findOne(Query.query(Criteria.where("productName").is(productName)), Product.class, "Product");
            if (product != null) {
                product.setQuantity(quantitySold);
            } else {
                product = new Product(productName, quantitySold);
            }
            mongoTemplate.save(product, "Product");
        }
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

    @Override
    public void run() {
        generateProducts();
        calculateTotalInvoices();
    }

    @PostConstruct
    public void scheduleJob() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SaleService.this.run();
            }
        };

        long delay = 0;
        long period = 24 * 60 * 60 * 1000;

        timer.schedule(task, delay, period);
    }
}
