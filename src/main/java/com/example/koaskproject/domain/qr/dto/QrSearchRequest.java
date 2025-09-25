package com.example.koaskproject.domain.qr.dto;

public record QrSearchRequest(
        String createdDate,
        String itemName,
        String serial
) {
}
