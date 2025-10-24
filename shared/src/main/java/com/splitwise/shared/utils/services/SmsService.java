package com.splitwise.shared.utils.services;


import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SmsService {

    private final List<SmsProvider> smsProviders;

    public void sendMessage(String to, String message) {

        // suppose we have several otp sms providers like Magfa , kaveh negar etc....
        for (SmsProvider sender : smsProviders) {
            try {
                boolean sent = sender.send(to, message);
                if (!sent) {
                    continue;
                }
                return;
            } catch (Exception e) {
                System.err.println(sender.getClass().getSimpleName() + " failed: " + e.getMessage());
            }
        }

        throw new SystemException(StatusCodes.SERVICER_UNAVAILABLE, ErrorCodes.SERVICE_UNAVAILABLE, "All sms senders failed");
    }
}
