package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Customer;
import com.zama.reto_sofka.model.ItemSales;
import com.zama.reto_sofka.model.Sales;
import com.zama.reto_sofka.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<?> registerSales(@RequestBody Sales sales) {
        List<String> errors = new ArrayList<>();

        if (sales.getItems() == null || sales.getItems().isEmpty()) {
            errors.add("La lista de items no puede estar vacía");
        } else {
            for (ItemSales item : sales.getItems()) {
                if (item.getName() == null || item.getName().isEmpty()) {
                    errors.add("El nombre del item no puede estar vacío");
                }
                if (item.getQuantity() <= 0) {
                    errors.add("La cantidad del item debe ser mayor a cero");
                }
                if (item.getPrice() <= 0) {
                    errors.add("El precio del item debe ser mayor a cero");
                }
            }
        }

        if (sales.getStoreLocation() == null || sales.getStoreLocation().isEmpty()) {
            errors.add("La ubicación de la tienda no puede estar vacía");
        }

        Customer customer = sales.getCustomer();
        if (customer == null) {
            errors.add("El cliente no puede ser nulo");
        } else {
            if (customer.getGender() == null || customer.getGender().isEmpty()) {
                errors.add("El género del cliente no puede estar vacío");
            }
            if (customer.getAge() <= 0) {
                errors.add("La edad del cliente debe ser mayor a cero");
            }
            if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
                errors.add("El email del cliente no puede estar vacío");
            }
            if (customer.getSatisfaction() < 1 || customer.getSatisfaction() > 5) {
                errors.add("La satisfacción del cliente debe estar entre 1 y 5");
            }
        }

        if (sales.getPurchaseMethod() == null || sales.getPurchaseMethod().isEmpty()) {
            errors.add("El método de compra no puede estar vacío");
        }

        if (!errors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", errors);
            response.put("fecha", new Date());
            return ResponseEntity.badRequest().body(response);
        }

        sales.setSaleDate(new Date());
        Sales registeredSales = saleService.registerSales(sales);

        boolean isRegistered = registeredSales != null;

        if (isRegistered) {
            return ResponseEntity.ok("Venta registrada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar la venta");
        }
    }

    @GetMapping("/get-invoices")
    public List<Sales> getPaginatedInvoices(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "100") int size) {
        return saleService.getPaginatedInvoices(page, size);
    }

}
