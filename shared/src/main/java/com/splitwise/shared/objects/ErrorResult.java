package com.splitwise.shared.objects;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {
    private Integer status;
    private Integer errorCode;
    private Object data;


    public ErrorResult(StatusCodes error, Integer errorCode, Object data) {
        this.errorCode = errorCode;
        this.data = data;
        this.status = error.getCode();
    }

    public ErrorResult(SystemException exception) {
        this.errorCode = exception.getErrorCode().getCode();
        this.data = exception.getArgument();
        this.status = exception.getStatusCode().getCode();
    }
}
