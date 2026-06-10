package com.axisbanking.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@ComponentScan(basePackages = {"com.axisbanking.insurance", "com.axisbanking.common"})
public class InsuranceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsuranceServiceApplication.class, args);
    }
}
