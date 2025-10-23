package com.splitwise.application.security;

import com.splitwise.application.models.dtos.auth.JwtTokenType;
import com.splitwise.application.models.dtos.auth.TokenResponse;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StausCodes;
import com.splitwise.shared.objects.SystemException;
import io.jsonwebtoken.Claims;
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
import java.util.function.Function;

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

    public TokenResponse refresh(String token) {
        validateToken(token, JwtTokenType.REFRESH_TOKEN);
        String userId = extractUserId(token, JwtTokenType.REFRESH_TOKEN);

        Map<String, Object> claims = new HashMap<>();
        String accessToken = buildToken(claims, Long.valueOf(userId), accessTokenExpiration, JwtTokenType.ACCESS_TOKEN);
        String refreshToken = buildToken(claims, Long.valueOf(userId), refreshTokenExpiration, JwtTokenType.REFRESH_TOKEN);
        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .ttl(accessTokenExpiration)
                .refreshTtl(refreshTokenExpiration)
                .creationTime(LocalDateTime.now())
                .build();
    }

    public void validateToken(String token, JwtTokenType type) {
        Boolean expired = isTokenExpired(token, type);
        if (expired) {
            throw new SystemException(StausCodes.ACCESS_DENIED, ErrorCodes.TOKEN_EXPIRED, "token expired");
        }
    }


    public String extractUserId(String token, JwtTokenType type) {
        return extractClaim(token, Claims::getSubject, type);
    }


    public Date extractExpiration(String token, JwtTokenType type) {
        return extractClaim(token, Claims::getExpiration, type);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, JwtTokenType type) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
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


    private Claims extractAllClaims(String token, JwtTokenType type) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey(type))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new SystemException(StausCodes.BAD_REQUEST, ErrorCodes.INVALID_TOKEN, "invalid token");
        }
    }

    private Boolean isTokenExpired(String token, JwtTokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

}