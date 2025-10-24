package com.splitwise.application.outbox;


import com.splitwise.application.models.entities.event.EventEntity;
import com.splitwise.application.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutBoxService {
    private final EventService eventService;

    @Scheduled(fixedRate = 20_000)
    @Transactional
    public void processEvents() {
        List<EventEntity> failedEvents = new ArrayList<>();
        List<EventEntity> successEvents = new ArrayList<>();

        List<EventEntity> events = eventService.getEvents();

        for (EventEntity event : events) {
            try {
                // send to message broker
                successEvents.add(event);
            } catch (Exception e) {
                event.setFailed(true);
                failedEvents.add(event);
            }
        }

        eventService.deleteEvents(successEvents);
        eventService.deleteEvents(failedEvents);
    }
}
