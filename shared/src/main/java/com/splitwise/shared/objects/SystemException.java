package com.splitwise.shared.objects;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SystemException extends RuntimeException {
    private final StatusCodes statusCode;
    private final ErrorCodes errorCode;
    private final Object argument;
}
