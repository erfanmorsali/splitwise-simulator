package com.splitwise.notification.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitwise.shared.brokers.models.GroupInviteEventMessage;
import com.splitwise.shared.statics.Topics;
import com.splitwise.shared.utils.services.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupInviteConsumer {

    private final SmsService smsService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topics.GROUP_INVITE, groupId = "group-invite")
    public void onGroupInvite(@Payload String stringPayload) throws JsonProcessingException {
        GroupInviteEventMessage payload = objectMapper.readValue(stringPayload, GroupInviteEventMessage.class);

        String message = null;
        switch (payload.getOperation()) {
            case ACCEPT ->
                    message = "user with id :" + payload.getUserId() + " accepted" + " invite for group with id :" + payload.getGroupId();
            case REJECT ->
                    message = "user with id :" + payload.getUserId() + " rejected" + " invite for group with id :" + payload.getGroupId();
        }

        System.out.println(message);
        // send any notif you want. for example sms
        smsService.sendMessage(payload.getGroupOwner(), message);
    }
}
