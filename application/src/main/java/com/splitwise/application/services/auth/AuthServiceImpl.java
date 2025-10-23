package com.splitwise.application.services.auth;

import com.splitwise.application.models.dtos.auth.OtpRequest;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.services.user.UserService;
import com.splitwise.application.utils.OtpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final OtpService otpService;

    @Override
    @Transactional(rollbackOn = Exception.class)
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
}
