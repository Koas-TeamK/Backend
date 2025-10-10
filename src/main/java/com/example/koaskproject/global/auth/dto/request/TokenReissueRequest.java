package com.example.koaskproject.global.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record TokenReissueRequest(
        @NotNull
        String refreshToken
) {
}
