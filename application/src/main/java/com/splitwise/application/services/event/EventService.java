package com.splitwise.application.services.event;

import com.splitwise.application.models.entities.event.EventEntity;

import java.util.List;

public interface EventService {
    void createEvent(Object payload, String topic);

    List<EventEntity> getEvents();
    void updateEvents(List<EventEntity> events);
    void deleteEvents(List<EventEntity> events);
}
