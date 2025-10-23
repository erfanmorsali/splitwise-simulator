package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum StausCodes {
    // General
    VALIDATION_EXCEPTION(400),
    BAD_REQUEST(400);

    private final Integer code;


    StausCodes(Integer code) {
        this.code = code;
    }

}
