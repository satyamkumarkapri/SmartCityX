package com.smartcityx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartCityXApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartCityXApplication.class, args);
    }
}
