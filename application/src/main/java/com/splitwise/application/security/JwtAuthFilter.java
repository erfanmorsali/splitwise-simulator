package com.splitwise.application.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitwise.application.models.dtos.auth.JwtOutputAuthentication;
import com.splitwise.application.models.dtos.auth.JwtTokenType;
import com.splitwise.application.models.dtos.auth.UserContextDto;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.ErrorResult;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                jwtService.validateToken(token, JwtTokenType.ACCESS_TOKEN);
                String userId = jwtService.extractUserId(token, JwtTokenType.ACCESS_TOKEN);
                UserContextDto userContextDto = new UserContextDto(Long.valueOf(userId));
                JwtOutputAuthentication authentication = new JwtOutputAuthentication(userContextDto);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                response.setStatus(StatusCodes.ACCESS_DENIED.getCode());
                response.getWriter().write(convertObjectToJson(new ErrorResult(new SystemException(StatusCodes.ACCESS_DENIED, ErrorCodes.INVALID_TOKEN, "access denied"))));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(Collections.singletonList(object));
    }
}