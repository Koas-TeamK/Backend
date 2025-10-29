package com.example.koaskproject.domain.news.dto;

import lombok.Builder;

@Builder
public record NewsSummary(
        Long id,
        String thumbnailUrl,
        String title,
        String date
) {
}
