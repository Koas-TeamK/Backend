package com.example.koaskproject.domain.qr.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class Qr
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String quUrl;

    private String message;

    private String createdDate;

    private String itemName;

    @Column(nullable = false, unique = true)
    private String serial;
}
