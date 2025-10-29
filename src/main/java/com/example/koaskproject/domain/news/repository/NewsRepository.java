package com.example.koaskproject.domain.news.repository;

import com.example.koaskproject.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query(value = """
    SELECT number
    FROM news
    ORDER BY number DESC
    LIMIT 1
    """, nativeQuery = true)
    Integer  findHighNumber();

    List<News> findTop5ByOrderByNumberDesc();

}
