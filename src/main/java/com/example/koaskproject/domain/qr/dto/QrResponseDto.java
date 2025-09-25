package com.example.koaskproject.domain.qr.dto;

import com.example.koaskproject.domain.qr.domain.Qr;


public record QrResponseDto(
        Long id,
        String image_url,
        String message,
        String createdDate,
        String itemName
)
{
    public static QrResponseDto from(Qr qr) {
        return new QrResponseDto(
                qr.getId(),
                qr.getImage_url(),
                qr.getMessage(),
                qr.getCreatedDate(),
                qr.getItemName()
        );
    }
}
