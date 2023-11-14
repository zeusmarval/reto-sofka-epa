package com.zama.reto_sofka.controller;

import com.zama.reto_sofka.model.Customer;
import com.zama.reto_sofka.model.ItemSales;
import com.zama.reto_sofka.model.Sales;
import com.zama.reto_sofka.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "Registrar una venta")
    @ApiResponse(responseCode = "200", description = "Venta registrada exitosamente", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Venta registrada exitosamente")))
    @ApiResponse(responseCode = "400", description = "Error en la solicitud", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{'error': ['La lista de items no puede estar vacía', 'El nombre del item no puede estar vacío', 'La cantidad del item debe ser mayor a cero', 'El precio del item debe ser mayor a cero', 'La ubicación de la tienda no puede estar vacía', 'El cliente no puede ser nulo', 'El género del cliente no puede estar vacío', 'La edad del cliente debe ser mayor a cero', 'El email del cliente no puede estar vacío', 'La satisfacción del cliente debe estar entre 1 y 5', 'El método de compra no puede estar vacío'], 'fecha': '2023-11-13T12:00:00'}")))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error al registrar la venta")))

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
    @Operation(summary = "Obtiene las facturas paginadas")
    public List<Sales> getPaginatedInvoices(
            @Parameter(description = "Número de página", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "100") @RequestParam(defaultValue = "100") int size) {
        return saleService.getPaginatedInvoices(page, size);
    }

}
