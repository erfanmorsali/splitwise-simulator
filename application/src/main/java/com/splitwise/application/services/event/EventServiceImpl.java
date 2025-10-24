package com.splitwise.application.services.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitwise.application.models.entities.event.EventEntity;
import com.splitwise.application.repositories.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public void createEvent(Object payload, String topic) {
        EventEntity event = new EventEntity();
        event.setTopic(topic);
        event.setPayload(convertPayloadToJson(payload));
        eventRepository.save(event);
    }

    @Override
    public List<EventEntity> getEvents() {
        return List.of();
    }

    @Override
    public void updateEvents(List<EventEntity> events) {
        return;
    }

    @Override
    public void deleteEvents(List<EventEntity> events) {
        return;
    }

    private String convertPayloadToJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert payload to JSON", e);
        }
    }
}
