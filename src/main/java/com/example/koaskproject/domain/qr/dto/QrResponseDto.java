package com.example.koaskproject.domain.qr.dto;

import com.example.koaskproject.domain.item.dto.ItemResponseDto;
import com.example.koaskproject.domain.item.dto.ItemTypeResponseDto;
import com.example.koaskproject.domain.qr.domain.Qr;

import java.time.LocalDate;

public record QrResponseDto(
        String image_url,
        String message,
        String createdDate
)
{
    public static QrResponseDto from(Qr qr) {
        return new QrResponseDto(
               qr.getImage_url(),
               qr.getMessage(),
               qr.getCreatedDate()
        );
    }
}
