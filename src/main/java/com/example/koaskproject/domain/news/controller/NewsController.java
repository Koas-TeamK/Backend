package com.example.koaskproject.domain.news.controller;

import com.example.koaskproject.domain.admin.dto.AdminLoginRequest;
import com.example.koaskproject.domain.admin.entity.Admin;
import com.example.koaskproject.domain.news.dto.NewsSummary;
import com.example.koaskproject.domain.news.entity.News;
import com.example.koaskproject.domain.news.repository.NewsRepository;
import com.example.koaskproject.domain.news.service.KoasCrawlerService;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.global.aop.RefreshTokenCheck;
import com.example.koaskproject.global.auth.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final KoasCrawlerService koasCrawlerService;

    @GetMapping("/summary")
    public ResponseEntity<List<NewsSummary>> getNewsSummary()
    {
        return ResponseEntity.ok(koasCrawlerService.getNewsSummary());
    }

    @GetMapping("/{id}")
    @RefreshTokenCheck
    public ResponseEntity<News> getNewsDetail(@PathVariable Long id) {
        return ResponseEntity.ok(koasCrawlerService.getNewsDetail(id));
    }

    @GetMapping("/crawlering")
    public ResponseEntity<Void> getCrawleringNews() throws IOException {
        koasCrawlerService.crawlNews();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllNews(){
        return ResponseEntity.ok(koasCrawlerService.getAllNews()) ;
    }

}
