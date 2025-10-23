package com.splitwise.application.models.dtos.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequest {
    @NotBlank
    private String code;
    @NotBlank
    @Size(min = 11, max = 11 , message = "mobile must be exactly 11 digits")
    private String mobile;
}
