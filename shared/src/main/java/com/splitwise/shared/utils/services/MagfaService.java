package com.splitwise.shared.utils.services;


import org.springframework.stereotype.Component;

@Component
public class MagfaService implements SmsProvider {

    @Override
    public boolean send(String destination, String message) {
        System.out.println("Sending message to " + destination);
        return true;
    }
}
