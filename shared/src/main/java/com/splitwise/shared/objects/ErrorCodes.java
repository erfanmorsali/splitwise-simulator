package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum ErrorCodes {
    // User
    USER_NOT_FOUND(1, "User Not Found"),


    // Otp
    INVALID_OTP(20, "Invalid OTP"),


    // General
    VALIDATION_EXCEPTION(5050, "Validation Exception"),;

    private final Integer code;
    private final String message;


    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
