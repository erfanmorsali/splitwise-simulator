package com.splitwise.application.services.auth;

import com.splitwise.application.models.dtos.auth.OtpRequest;

public interface AuthService {
    String requestOtp(OtpRequest request);
}
