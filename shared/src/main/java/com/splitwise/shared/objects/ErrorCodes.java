package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum ErrorCodes {
    // User
    USER_NOT_FOUND(1, "User not found"),


    // Otp
    INVALID_OTP(20, "Invalid OTP");


    private final Integer code;
    private final String message;


    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
