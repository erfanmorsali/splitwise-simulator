package com.splitwise.application.security;

import com.splitwise.application.models.dtos.auth.JwtTokenType;
import com.splitwise.application.models.dtos.auth.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret.access.key}")
    private String accessTokenSecret;
    @Value("${jwt.secret.refresh.key}")
    private String refreshTokenSecret;
    @Value("${jwt.token.access.expire}")
    private Long accessTokenExpiration;
    @Value("${jwt.token.refresh.expire}")
    private Long refreshTokenExpiration;


    public TokenResponse create(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        String accessToken = buildToken(claims, userId, accessTokenExpiration, JwtTokenType.ACCESS_TOKEN);
        String refreshToken = buildToken(claims, userId, refreshTokenExpiration, JwtTokenType.REFRESH_TOKEN);
        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .ttl(accessTokenExpiration)
                .refreshTtl(refreshTokenExpiration)
                .creationTime(LocalDateTime.now())
                .build();
    }

    private String buildToken(Map<String, Object> claims, Long userId, Long expiration, JwtTokenType type) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiration))
                .signWith(getSignKey(type), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(JwtTokenType type) {
        String tokenSecret = type.equals(JwtTokenType.ACCESS_TOKEN) ? accessTokenSecret : refreshTokenSecret;

        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}