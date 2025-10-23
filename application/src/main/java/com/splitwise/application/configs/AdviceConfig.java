package com.splitwise.application.configs;

import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.ErrorResult;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;


@RestControllerAdvice
public class AdviceConfig {

    @ExceptionHandler(SystemException.class)
    public List<ErrorResult> handleSystemException(SystemException exception, HttpServletResponse response) {
        response.setStatus(exception.getStatusCode().getCode());
        return Collections.singletonList(new ErrorResult(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResult> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletResponse response) {
        List<ErrorResult> validations = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            Map<String, Object> data = new HashMap<>();

            data.put("fieldName", ((FieldError) error).getField());
            data.put("errorMessage", error.getDefaultMessage());
            validations.add(new ErrorResult(StatusCodes.VALIDATION_EXCEPTION, ErrorCodes.VALIDATION_EXCEPTION.getCode(), data));
        });

        response.setStatus(StatusCodes.VALIDATION_EXCEPTION.getCode());
        return validations;
    }
}
