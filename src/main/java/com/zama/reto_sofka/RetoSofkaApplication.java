package com.zama.reto_sofka;

import com.zama.reto_sofka.job.SalesJob;
import com.zama.reto_sofka.job.ProductJob;
import com.zama.reto_sofka.service.SaleService;
import com.zama.reto_sofka.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RetoSofkaApplication {

	private static SalesJob salesJob;
	private static ProductJob productJob;

	@Autowired
	public void setSalesJob(SalesJob salesJob) {
		RetoSofkaApplication.salesJob = salesJob;
	}

	@Autowired
	public void setProductJob(ProductJob productJob) {
		RetoSofkaApplication.productJob = productJob;
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RetoSofkaApplication.class, args);

		SaleService saleService = context.getBean(SaleService.class);
		ProductService productService = context.getBean(ProductService.class);

		salesJob.salesScheduleJob();
		productJob.productScheduleJob();
	}
}
