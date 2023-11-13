package com.zama.reto_sofka.job;

import com.zama.reto_sofka.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class SalesJob implements Runnable {

    private final MongoTemplate mongoTemplate;
    private final SaleService saleService;

    @Autowired
    public SalesJob(MongoTemplate mongoTemplate, SaleService saleService) {
        this.mongoTemplate = mongoTemplate;
        this.saleService = saleService;
    }

    @Override
    public void run() {
        saleService.calculateTotalInvoices();
    }

    @PostConstruct
    public void salesScheduleJob() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SalesJob.this.run();
            }
        };

        long delay = 0;
        long period = 10 * 60 * 1000;

        timer.schedule(task, delay, period);
    }
}
