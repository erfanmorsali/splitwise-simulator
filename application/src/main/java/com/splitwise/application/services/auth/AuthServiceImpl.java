package com.splitwise.application.services.auth;

import com.splitwise.application.models.dtos.auth.OtpRequest;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Override
    public String requestOtp(OtpRequest request) {
        String mobile = request.getMobile();

        Optional<UserEntity> optionalUser = userService.findByMobile(mobile);

        if (optionalUser.isPresent()) {

            // check for sending otp or not
        } else {
            UserEntity entity = new UserEntity();
            entity.setMobile(mobile);
            entity.setName(mobile);
            userService.save(entity);

            // send otp
        }

        return "OtpCode";
    }
}
