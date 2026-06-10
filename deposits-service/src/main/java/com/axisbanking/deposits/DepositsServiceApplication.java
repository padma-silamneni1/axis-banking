package com.axisbanking.deposits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DepositsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepositsServiceApplication.class, args);
    }
}
