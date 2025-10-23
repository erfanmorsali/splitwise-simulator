package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum ErrorCodes {
    // User
    USER_NOT_FOUND(1, "User Not Found"),


    // Security
    INVALID_OTP(20, "Invalid OTP"),
    INVALID_TOKEN(21, "Invalid Token"),
    TOKEN_EXPIRED(22, "Token Expired"),


    // General
    VALIDATION_EXCEPTION(5050, "Validation Exception");

    private final Integer code;
    private final String message;


    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
