package com.splitwise.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@ComponentScan(value = {"com.splitwise.shared", "com.splitwise.application"})
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Api {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Api.class, args);
    }
}
