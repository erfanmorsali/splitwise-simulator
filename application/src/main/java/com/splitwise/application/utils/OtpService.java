package com.splitwise.application.utils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {
    @Value("${otp.duration}")
    private Integer otpDuration;
    private final RedisTemplate<String, String> redisTemplate;


    public OtpResponse generateOtp(String key) {
        OtpResponse otpResponse = new OtpResponse();

        String code = redisTemplate.opsForValue().get(key);
        if (code == null) {
            int MIN = 100000;
            int MAX = 999999;

            code = String.valueOf(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
            redisTemplate.opsForValue().set(key, code);
            redisTemplate.expire(key, otpDuration, TimeUnit.SECONDS);
            otpResponse.setSendOtp(true);
        }

        otpResponse.setCode(code);
        return otpResponse;
    }

    public boolean validateOtp(String key, String inputCode) {
        String redisKey = redisTemplate.opsForValue().get(key);

        if (redisKey != null && redisKey.equals(inputCode)) {
            redisTemplate.delete(key);
            return true;
        }

        return false;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class OtpResponse {

        private boolean sendOtp;
        private String code;
    }

}
