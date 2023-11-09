package com.zama.reto_sofka;

import com.zama.reto_sofka.service.FacturaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RetoSofkaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RetoSofkaApplication.class, args);

		FacturaService facturaService = context.getBean(FacturaService.class);
		facturaService.generarProductos();
		facturaService.calcularTotalFacturas();
	}

}
