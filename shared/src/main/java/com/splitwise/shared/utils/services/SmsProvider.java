package com.splitwise.shared.utils.services;


public interface SmsProvider {
    boolean send(String destination, String message);
}
