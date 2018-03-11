package com.mea.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Michael Jou
 */
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
@EnableAsync
@ComponentScan({"com.mea.site","cn.afterturn.easypoi.view"})
public class MeaSiteBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeaSiteBootApplication.class, args);
    }
}
