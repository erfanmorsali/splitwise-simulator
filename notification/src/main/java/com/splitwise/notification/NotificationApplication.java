package com.splitwise.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@ComponentScan(value = {"com.splitwise.shared", "com.splitwise.notification"})
@SpringBootApplication
public class NotificationApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(NotificationApplication.class, args);
    }
}
