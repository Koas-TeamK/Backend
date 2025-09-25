package com.example.koaskproject.global.auth.service;


import com.example.koaskproject.global.auth.RefreshToken.RefreshToken;
import com.example.koaskproject.global.auth.RefreshToken.RefreshTokenRepository;
import com.example.koaskproject.global.auth.dto.response.TokenResponse;
import com.example.koaskproject.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final Duration REFRESH_TOKEN_EXP = Duration.ofDays(1);
    private static final Duration ACCESS_TOKEN_EXP = Duration.ofMinutes(30);

    @Transactional
    public TokenResponse login(Long id, String role)
    {

        String accessToken = jwtProvider.generateToken(id,role,ACCESS_TOKEN_EXP);
        String refreshToken = jwtProvider.generateToken(id,role,REFRESH_TOKEN_EXP);

        refreshTokenRepository.findByUserId(id)
                .ifPresentOrElse(
                        existing -> existing.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(RefreshToken.of(id, refreshToken))
                );

        return TokenResponse.of(accessToken);
    }

    public Long getUserId()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(principal);

        if (principal instanceof Long) {
            return (Long) principal;
        } else if (principal instanceof String) {
            return Long.valueOf((String) principal);
        } else {
            throw new IllegalStateException("Principal 타입 알 수 없음: " + principal.getClass());
        }
    }
}
