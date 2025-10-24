package com.splitwise.application.services.messageBroker;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaService implements MessageBroker {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(Object message, String topic) {
        System.out.println("sending message to kafka");
        kafkaTemplate.send(topic, message);
    }
}
