package com.splitwise.application.services.auth;

import com.splitwise.application.models.dtos.auth.OtpRequest;
import com.splitwise.application.models.dtos.auth.TokenResponse;
import com.splitwise.application.models.dtos.auth.VerifyOtpRequest;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.security.JwtService;
import com.splitwise.application.services.user.UserService;
import com.splitwise.application.utils.OtpService;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StausCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final OtpService otpService;
    private final JwtService jwtService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean requestOtp(OtpRequest request) {
        String mobile = request.getMobile();

        Optional<UserEntity> optionalUser = userService.findByMobile(mobile);

        if (optionalUser.isEmpty()) {
            UserEntity entity = new UserEntity();
            entity.setMobile(mobile);
            entity.setName(mobile);
            userService.save(entity);
        }

        OtpService.OtpResponse otpResponse = otpService.generateOtp(mobile);

        if (!otpResponse.isSendOtp()) {
            return false; // we sent code in last x minutes . so we should not send again
        }

        // send sms
        System.out.println(otpResponse.getCode());
        return true;
    }

    @Transactional(readOnly = true)
    public TokenResponse verifyOtp(VerifyOtpRequest request) {
        UserEntity user = userService.findByMobile(request.getMobile())
                .orElseThrow(() -> new SystemException(StausCodes.BAD_REQUEST, ErrorCodes.USER_NOT_FOUND, "Invalid mobile"));

        boolean isValid = otpService.validateOtp(user.getMobile(), request.getCode());
        if (!isValid) {
            throw new SystemException(StausCodes.BAD_REQUEST, ErrorCodes.INVALID_OTP, "Invalid OTP");
        }

        return jwtService.create(user.getId());
    }
}
