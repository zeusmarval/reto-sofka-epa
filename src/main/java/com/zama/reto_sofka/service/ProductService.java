package com.zama.reto_sofka.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.zama.reto_sofka.model.ItemSales;
import com.zama.reto_sofka.model.Sales;
import com.zama.reto_sofka.model.Product;
import com.zama.reto_sofka.model.SoldProducts;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProductService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<SoldProducts> mostSelledProducts(int limit, String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicioISO;
        Date fechaFinISO;

        try {
            fechaInicioISO = dateFormat.parse(startDate);
            fechaFinISO = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("saleDate").gte(fechaInicioISO).lt(fechaFinISO));

        List<Sales> ventas = mongoTemplate.find(query, Sales.class, "Sales");

        Map<String, Integer> productCount = new HashMap<>();

        for (Sales venta : ventas) {
            List<ItemSales> items = venta.getItems();

            for (ItemSales item : items) {

                String productName = item.getName();
                int quantity = item.getQuantity();

                if (productCount.containsKey(productName)) {
                    int currentCount = productCount.get(productName);
                    productCount.put(productName, currentCount + quantity);
                } else {
                    productCount.put(productName, quantity);
                }
            }
        }

        List<SoldProducts> selledProduct = new ArrayList<>();
        productCount.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .forEach(entry -> {
                    SoldProducts soldProducts = new SoldProducts(entry.getKey(), entry.getValue());
                    selledProduct.add(soldProducts);
                });

        return selledProduct;
    }

    public static List<Product> allSelled(MongoTemplate mongoTemplate) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection("Product");

        MongoCursor<Document> cursor = collection.find().iterator();

        List<Product> products = new ArrayList<>();

        while (cursor.hasNext()) {
            Document productoDocument = cursor.next();
            String id = productoDocument.get("_id").toString();
            String name = productoDocument.getString("productName");
            int quantity = productoDocument.getInteger("quantity");

            Product product = new Product(id, name, quantity);
            products.add(product);
        }

        return products;
    }

}
