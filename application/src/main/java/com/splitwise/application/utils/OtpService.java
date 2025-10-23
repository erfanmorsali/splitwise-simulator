package com.splitwise.application.utils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

    public OtpResponse generateOtp(String key) {
        return new OtpResponse();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public class OtpResponse {

        private boolean sendOtp;
        private String code;
    }

}
