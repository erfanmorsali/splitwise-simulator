package com.splitwise.application.services.auth;

import com.splitwise.application.models.dtos.auth.OtpRequest;
import com.splitwise.application.models.dtos.auth.RefreshTokenRequest;
import com.splitwise.application.models.dtos.auth.TokenResponse;
import com.splitwise.application.models.dtos.auth.VerifyOtpRequest;

public interface AuthService {
    String requestOtp(OtpRequest request);
    TokenResponse verifyOtp(VerifyOtpRequest request);
    TokenResponse refresh(RefreshTokenRequest request);
}
