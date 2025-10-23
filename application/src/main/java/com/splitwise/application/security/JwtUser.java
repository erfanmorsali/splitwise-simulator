package com.splitwise.application.security;

import com.splitwise.application.models.dtos.auth.UserContextDto;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUser {

    public static UserContextDto getAuthenticatedUser() throws SystemException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserContextDto userContextDto) {
            return userContextDto;
        }
        throw new SystemException(StatusCodes.ACCESS_DENIED, ErrorCodes.ACCESS_DENIED, "cant get user");
    }
}
