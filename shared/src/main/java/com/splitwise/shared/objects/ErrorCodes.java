package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum ErrorCodes {
    // User
    USER_NOT_FOUND(1, "User Not Found"),
    USER_SUSPENDED(2, "User Is Suspended"),


    // Security
    INVALID_OTP(20, "Invalid OTP"),
    INVALID_TOKEN(21, "Invalid Token"),
    TOKEN_EXPIRED(22, "Token Expired"),
    ACCESS_DENIED(23, "Access Denied"),

    // Group
    GROUP_NOT_FOUND(40, "Group Not Found"),
    NOT_MEMBER_OF_GROUP(41, "User Is Not Member Of This Group"),
    NOT_OWNER_OF_GROUP(42, "User Is Not Owner Of This Group"),
    GROUP_INVITE_NOT_FOUND(43, "Group Invite Not Found"),
    GROUP_INVITE_NOT_CHANGEABLE(44, "Group Invite Is Already Accepted Or Rejected"),
    USER_ALREADY_INVITED_TO_GROUP(45, "User Already Invited To Group"),
    USER_ALREADY_MEMBER_OF_GROUP(46, "User Already Is Member Of This Group"),

    // Cost
    COST_NOT_FOUND(60, "Cost Not Found"),
    NOT_OWNER_OF_COST(60, "User Is  Not Owner Of This Cost"),


    // General
    SERVICE_UNAVAILABLE(4040, "Service Unavailable"),
    VALIDATION_EXCEPTION(5050, "Validation Exception");

    private final Integer code;
    private final String message;


    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
