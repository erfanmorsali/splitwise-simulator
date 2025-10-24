package com.splitwise.application.configs;

import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.RateLimited;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

import static com.splitwise.shared.utils.services.ClientIpInfoService.getMainIP;

@Aspect
@Component
@RequiredArgsConstructor
public class AspectConfig {
    private final RedisTemplate<String, String> redisTemplate;


    @Around("@annotation(rateLimited)")
    public Object rateLimitByIp(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getMainIP(request);

        String key = "rate_limit:" + ip;
        Long requestCount = redisTemplate.opsForValue().increment(key, 1);

        if (requestCount == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(rateLimited.duration()));
        }

        if (requestCount > rateLimited.limit()) {
            throw new SystemException(StatusCodes.TOO_MANY_REQUESTS, ErrorCodes.TOO_MANY_REQUESTS, "too many requests");
        }

        return joinPoint.proceed();
    }
}
