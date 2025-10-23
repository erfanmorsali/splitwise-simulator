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
    private SystemError code;
    private Integer status;
    private Integer errorCode;
    private Object data;


    public ErrorResult(SystemError error, Integer errorCode, Object data) {
        this.code = error;
        this.errorCode = errorCode;
        this.data = data;
        this.status = error.getValue();
    }
}
