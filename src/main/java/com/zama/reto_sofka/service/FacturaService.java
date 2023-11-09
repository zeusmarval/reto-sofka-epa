package com.zama.reto_sofka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Service
public class FacturaService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public FacturaService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void generarProductos() {
        List<Factura> facturas = mongoTemplate.findAll(Factura.class, "Sales");

        for (Factura factura : facturas) {
            for (ItemFactura item : factura.getItems()) {
                Producto producto = new Producto();
                producto.setProductName(item.getProductName());
                producto.setQuantity(item.getQuantity());

                mongoTemplate.save(producto, "Productos");
            }
        }
    }

    public void calcularTotalFacturas() {
        List<Factura> facturas = mongoTemplate.findAll(Factura.class, "Sales");

        for (Factura factura : facturas) {
            double totalValue = 0.0;

            for (ItemFactura item : factura.getItems()) {
                totalValue += item.getPrice() * item.getQuantity();
            }

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(factura.getId()));
            Update update = new Update();
            update.set("totalValue", totalValue);

            mongoTemplate.updateFirst(query, update, Factura.class, "Sales");
        }
    }
}
