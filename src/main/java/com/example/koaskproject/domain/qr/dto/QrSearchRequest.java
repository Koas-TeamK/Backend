package com.example.koaskproject.domain.qr.dto;

public record QrSearchRequest(
        String startDate,
        String endDate,
        String itemName,
        String serial
) {
}
