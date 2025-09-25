package com.example.koaskproject.domain.qr.dto;

import com.example.koaskproject.domain.item.entity.Item;
import com.example.koaskproject.domain.qr.domain.Qr;

import java.time.LocalDate;
import java.util.List;


public record CheckQrResponseDto(
        List<String> imageUrl,
        List<String> videoUrl,
        String serial,
        String itemName,
        String message,
        String createdDate
)
{
    public static CheckQrResponseDto from(Qr qr, Item item) {
        return new CheckQrResponseDto(
                item.getItemType().getImageUrl(),
                item.getItemType().getVideoUrl(),
                item.getLimitedNumber(),
                item.getItemType().getName(),
                qr.getMessage(),
                qr.getCreatedDate()

        );
    }
}
