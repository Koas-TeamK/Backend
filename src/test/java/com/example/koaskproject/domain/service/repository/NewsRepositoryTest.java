package com.example.koaskproject.domain.service.repository;


import com.example.koaskproject.domain.news.entity.News;
import com.example.koaskproject.domain.news.repository.NewsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2 사용
public class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;

    @BeforeEach
    public void setUp() {
        newsRepository.save(News.builder().number(2).build());
    }

    @Test
    @Transactional
    public void findTop5ByOrderByNumberDescTest() {

        List<News> news1=newsRepository.findTop5ByOrderByNumberDesc();
        List<News> news2=newsRepository.findTop5ByOrderByNumberDesc();
        Assertions.assertSame(news1.get(0), news2.get(0));
    }

    @Test
    @Transactional
    public void findTop5ByOrderByNumberDescTest2() {

        News news1=newsRepository.findById(2L).get();
        News news2=newsRepository.findById(2L).get();
        Assertions.assertSame(news1, news2);
    }
}
