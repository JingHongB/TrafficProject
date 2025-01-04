package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrafficBackendApplication {
//test
    public static void main(String[] args) {
        SpringApplication.run(TrafficBackendApplication.class, args);
    }

}
