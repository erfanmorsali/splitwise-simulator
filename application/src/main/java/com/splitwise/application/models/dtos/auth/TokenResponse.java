package com.splitwise.application.models.dtos.auth;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long ttl;
    private Long refreshTtl;
    private LocalDateTime creationTime = LocalDateTime.now();
}
