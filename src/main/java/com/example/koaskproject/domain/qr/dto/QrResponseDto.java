package com.example.koaskproject.domain.qr.dto;

import com.example.koaskproject.domain.qr.domain.Qr;


public record QrResponseDto(
        Long id,
        String qrUrl,
        String imageUrl,
        String message,
        String createdDate,
        String itemName,
        String serial
)
{
    public static QrResponseDto from(Qr qr) {
        return new QrResponseDto(
                qr.getId(),
                qr.getQuUrl(),
                qr.getImageUrl(),
                qr.getMessage(),
                qr.getCreatedDate(),
                qr.getItemName(),
                qr.getSerial()
        );
    }
}
