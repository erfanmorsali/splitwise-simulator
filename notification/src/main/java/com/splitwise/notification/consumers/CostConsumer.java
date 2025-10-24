package com.splitwise.notification.consumers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitwise.shared.brokers.models.CostEventMessage;
import com.splitwise.shared.statics.Topics;
import com.splitwise.shared.utils.services.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CostConsumer {
    private final ObjectMapper objectMapper;
    private final SmsService smsService;

    @KafkaListener(topics = Topics.COST, groupId = "costs")
    public void onGroupInvite(@Payload String stringPayload) throws JsonProcessingException {
        CostEventMessage payload = objectMapper.readValue(stringPayload, CostEventMessage.class);

        String message = null;
        switch (payload.getOperation()) {
            case CREATED ->
                    message = "user with id :" + payload.getCreatorId() + " created cost with title : " + payload.getTitle() + "with this amount : " + payload.getAmount();
            case UPDATED ->
                    message = "user with id :" + payload.getCreatorId() + " updated cost with title : " + payload.getTitle() + "with this amount : " + payload.getAmount();
        }

        System.out.println(message);
        // send any notif you want. for example sms

        for (String involvedUser : payload.getInvolvedUserMobiles()) {
            smsService.sendMessage(involvedUser, message);
        }
    }
}
