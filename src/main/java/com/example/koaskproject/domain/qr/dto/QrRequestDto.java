package com.example.koaskproject.domain.qr.dto;

import com.example.koaskproject.domain.qr.domain.Qr;

public record QrRequestDto(
    String qrUrl,
    String image,
    String serial,
    String message,
    String createdDate,
    String itemName
) {
    public static Qr of (QrRequestDto dto) {
        return Qr.builder()
                .quUrl(dto.qrUrl)
                .imageUrl(dto.image)
                .serial(dto.serial)
                .message(dto.message)
                .createdDate(dto.createdDate)
                .itemName(dto.itemName)
                .build();
    }
}
