package com.splitwise.application.services.messageBroker;

public interface MessageBroker {
    void sendMessage(Object message, String topic);
}
