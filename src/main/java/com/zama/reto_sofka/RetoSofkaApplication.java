package com.zama.reto_sofka;

import com.zama.reto_sofka.service.SaleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RetoSofkaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RetoSofkaApplication.class, args);

		SaleService saleService = context.getBean(SaleService.class);

		// Ejecutar el job una vez al iniciar la aplicación
		saleService.run();

		// Programar el job para que se ejecute periódicamente
		saleService.scheduleJob();
	}

}
