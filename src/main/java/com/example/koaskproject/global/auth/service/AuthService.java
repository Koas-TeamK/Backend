package com.example.koaskproject.global.auth.service;


import com.example.koaskproject.domain.admin.entity.Admin;
import com.example.koaskproject.domain.admin.repository.AdminRepository;
import com.example.koaskproject.global.auth.RefreshToken.RefreshToken;
import com.example.koaskproject.global.auth.RefreshToken.RefreshTokenRepository;
import com.example.koaskproject.global.auth.dto.response.TokenResponse;
import com.example.koaskproject.global.auth.jwt.JwtProvider;
import com.example.koaskproject.global.exception.ErrorCode;
import com.example.koaskproject.global.exception.GlobalException;
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
    private final AdminRepository adminRepository;
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

        return TokenResponse.of(accessToken,role);
    }

    @Transactional
    public void logout()
    {
        Long userId = getUserId();
        try {

            refreshTokenRepository.deleteByUserId(userId);
        }
        catch (Exception e) {
        throw new GlobalException(ErrorCode.R_TOKEN_DELETE_FAIL);
    }
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

    public TokenResponse reissue()
    {
        Long userId= getUserId();

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElse(null);

        if(jwtProvider.validateToken(refreshToken.getRefreshToken()))
            throw new GlobalException(ErrorCode.INVALID_TOKEN,"리프레쉬토큰이 유효하지 않습니다. 로그인을 다시 해주세요");

        Admin admin =adminRepository.findById(userId).orElse(null);

        return TokenResponse.of(
                jwtProvider.generateToken(userId,admin.getRole().toString(),ACCESS_TOKEN_EXP),admin.getRole().toString());
    }
}
