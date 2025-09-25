package com.example.koaskproject.domain.qr.dto;

import com.example.koaskproject.domain.qr.domain.Qr;

public record QrRequestDto(
    String imageUrl,
    String serial,
    String message,
    String createdDate,
    String itemName
) {
    public static Qr of (QrRequestDto dto) {
        return Qr.builder()
                .image_url(dto.imageUrl)
                .serial(dto.serial)
                .message(dto.message)
                .createdDate(dto.createdDate)
                .itemName(dto.itemName)
                .build();
    }
}
