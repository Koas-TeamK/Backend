package com.example.koaskproject.global.aop;

import com.example.koaskproject.global.auth.RefreshToken.RefreshToken;
import com.example.koaskproject.global.auth.RefreshToken.RefreshTokenRepository;
import com.example.koaskproject.global.auth.jwt.JwtProvider;
import com.example.koaskproject.global.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class JwtRefreshAspect {

    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Around("@annotation(com.example.koaskproject.global.aop.RefreshTokenCheck)")
    public Object refreshAccessToken(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        String accessToken = jwtProvider.resolveAccessToken();
        if(accessToken == null)  return joinPoint.proceed();
        if (jwtProvider.isExpired(accessToken)) {
            Long userId = authService.getUserId();
            RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Refresh Token not found"));

            if (refreshToken!=null && jwtProvider.validateToken(refreshToken.getRefreshToken())) {

                String newAccessToken = jwtProvider.generateToken(userId,"users", Duration.ofHours(1));
                response.setHeader("Authorization", "Bearer " + newAccessToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return null;
            }
        }

        return joinPoint.proceed();
    }
}
