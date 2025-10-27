package com.example.koaskproject.domain.item.dto;

import com.example.koaskproject.domain.item.entity.ItemType;

import java.util.List;

public record ItemTypeResponseDto(
        String name,
        String description
) {
    public static ItemTypeResponseDto from(ItemType itemType) {
        return new ItemTypeResponseDto(
                itemType.getName(),
                itemType.getDescription()
        );
    }
}
