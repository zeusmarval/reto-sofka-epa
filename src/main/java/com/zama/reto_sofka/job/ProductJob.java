package com.zama.reto_sofka.job;

import com.zama.reto_sofka.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ProductJob implements Runnable {

    private final MongoTemplate mongoTemplate;
    private final ProductService productService;

    @Autowired
    public ProductJob(MongoTemplate mongoTemplate, ProductService productService) {
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
    }

    @Override
    public void run() {
        ProductService.generateProducts(mongoTemplate);
    }

    @PostConstruct
    public void productScheduleJob() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ProductJob.this.run();
            }
        };

        long delay = 0;
        long period = 24 * 60 * 60 * 1000;

        timer.schedule(task, delay, period);
    }
}
