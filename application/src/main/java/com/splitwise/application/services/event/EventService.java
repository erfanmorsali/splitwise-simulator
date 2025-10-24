package com.splitwise.application.services.event;

public interface EventService {
    void createEvent(Object payload, String topic);
}
