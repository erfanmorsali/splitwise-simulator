package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum StatusCodes {
    // General
    VALIDATION_EXCEPTION(400),
    BAD_REQUEST(400),
    ACCESS_DENIED(401),
    DATA_NOT_FOUND(404),
    FORBIDDEN(403);

    private final Integer code;


    StatusCodes(Integer code) {
        this.code = code;
    }

}
