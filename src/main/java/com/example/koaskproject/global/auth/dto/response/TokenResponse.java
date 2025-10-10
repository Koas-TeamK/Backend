package com.example.koaskproject.global.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
        String role,
        String accessToken

) {
    public static TokenResponse of(String accessToken, String role) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .role(role)
                .build();
    }
}
