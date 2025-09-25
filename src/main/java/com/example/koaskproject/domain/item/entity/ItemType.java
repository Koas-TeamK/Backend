package com.example.koaskproject.domain.item.entity;

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
public class ItemType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @Column(length = 500)
    @ElementCollection
    private final List<String> imageUrl= new ArrayList<>();

    @Column(length = 500)
    @ElementCollection
    private final List<String> videoUrl= new ArrayList<>();
}
