package com.example.koaskproject.domain.item.dto;

import com.example.koaskproject.domain.item.entity.Item;

import java.time.LocalDateTime;

public record ItemResponseDto(
        Long id,
        String limitedNumber,
        ItemTypeResponseDto itemType
) {
    public static ItemResponseDto from(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getLimitedNumber(),
                ItemTypeResponseDto.from(item.getItemType())
        );
    }
}
