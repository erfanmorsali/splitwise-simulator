package com.splitwise.application.controllers.auth;


import com.splitwise.application.models.dtos.auth.OtpRequest;
import com.splitwise.application.models.dtos.auth.RefreshTokenRequest;
import com.splitwise.application.models.dtos.auth.TokenResponse;
import com.splitwise.application.models.dtos.auth.VerifyOtpRequest;
import com.splitwise.application.services.auth.AuthService;
import com.splitwise.application.statics.Urls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${rest.auth}")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;


    @PostMapping(Urls.REQUEST_OTP)
    public ResponseEntity<String> requestOtp(@Valid @RequestBody OtpRequest request) {
        return new ResponseEntity<>(service.requestOtp(request), HttpStatus.OK);
    }

    @PostMapping(Urls.VERIFY_OTP)
    public ResponseEntity<TokenResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return new ResponseEntity<>(service.verifyOtp(request), HttpStatus.OK);
    }

    @PostMapping(Urls.REFRESH)
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return new ResponseEntity<>(service.refresh(request), HttpStatus.OK);
    }

}
