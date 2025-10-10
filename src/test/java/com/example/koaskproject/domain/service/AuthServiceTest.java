package com.example.koaskproject.domain.service;

import com.example.koaskproject.domain.admin.entity.Role;
import com.example.koaskproject.domain.qr.service.QrService;
import com.example.koaskproject.global.auth.RefreshToken.RefreshToken;
import com.example.koaskproject.global.auth.RefreshToken.RefreshTokenRepository;
import com.example.koaskproject.global.auth.dto.response.TokenResponse;
import com.example.koaskproject.global.auth.jwt.JwtProvider;
import com.example.koaskproject.global.auth.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Optional;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    private final Long userId = 1L;
    private final String role = Role.ADMIN.toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService( jwtProvider,refreshTokenRepository,null);
    }
    @Test
    @DisplayName("로그인 시 accessToken + refreshToken 생성 및 저장")
    void loginSuccess_NewRefreshToken() {


        Mockito.when(refreshTokenRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        TokenResponse response = authService.login(userId, role);
        System.out.println(response);

        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("로그인 시 기존 refreshToken이 있으면 updateToken 호출")
    void loginSuccess_ExistingRefreshToken() {


        RefreshToken existingToken = Mockito.mock(RefreshToken.class);

        Mockito.when(refreshTokenRepository.findByUserId(userId))
                .thenReturn(Optional.of(existingToken));

        // when
        TokenResponse response = authService.login(userId, role);

        // then
        Assertions.assertNotNull(response);
        Mockito.verify(existingToken).updateToken(Mockito.anyString());
        Mockito.verify(refreshTokenRepository, Mockito.never()).save(Mockito.any());
    }
}