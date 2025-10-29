package com.example.koaskproject.domain.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class News
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int number;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String date;

    @ElementCollection
    @CollectionTable(
            name = "news_images",
            joinColumns = @JoinColumn(name = "news_id")
    )
    @Column(name = "img_url", columnDefinition = "TEXT")
    private List<String> imgUrl = new ArrayList<>();

}
