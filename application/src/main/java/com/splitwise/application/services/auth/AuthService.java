package com.splitwise.application.services.auth;

import com.splitwise.application.models.dtos.auth.OtpRequest;
import com.splitwise.application.models.dtos.auth.TokenResponse;
import com.splitwise.application.models.dtos.auth.VerifyOtpRequest;

public interface AuthService {
    Boolean requestOtp(OtpRequest request);
    TokenResponse verifyOtp(VerifyOtpRequest request);
}
