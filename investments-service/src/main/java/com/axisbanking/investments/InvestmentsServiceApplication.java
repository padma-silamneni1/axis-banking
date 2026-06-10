package com.axisbanking.investments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@ComponentScan(basePackages = {"com.axisbanking.investments", "com.axisbanking.common"})
public class InvestmentsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvestmentsServiceApplication.class, args);
    }
}
