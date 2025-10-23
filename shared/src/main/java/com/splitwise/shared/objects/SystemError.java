package com.splitwise.shared.objects;


import lombok.Getter;

@Getter
public enum SystemError {
    VALIDATION_EXCEPTION(400);

    private final Integer value;

    SystemError(Integer value) {
        this.value = value;
    }

}
