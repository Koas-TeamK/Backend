package com.example.koaskproject.domain.service;


import com.example.koaskproject.domain.news.dto.NewsSummary;
import com.example.koaskproject.domain.news.entity.News;
import com.example.koaskproject.domain.news.repository.NewsRepository;
import com.example.koaskproject.domain.news.service.KoasCrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class KoasCrawlerTestWithDB
{
    @Autowired
    private KoasCrawlerService koasCrawlerService;

    @Autowired
    private NewsRepository newsRepository;
    @Test
    public void crawlingTest() throws IOException {
        koasCrawlerService.crawlNews();

        List<News> newsList =newsRepository.findAll();

        Assertions.assertNotNull(newsList);

        System.out.println(newsList.size());

        for(News news:newsList)
        {
            System.out.println(news.getNumber()+" "+news.getTitle());
        }

        koasCrawlerService.crawlNews();

        Assertions.assertEquals(newsList.size(), newsRepository.findAll().size());

        List<NewsSummary> newsSummaryList= koasCrawlerService.getNewsSummary();
        for(NewsSummary newsSummary:newsSummaryList)
        {
            System.out.println(newsSummary.title()+" "+newsSummary.date());
        }

        News news=koasCrawlerService.getNewsDetail(newsSummaryList.get(0).id());
        System.out.println(news.getTitle());
    }
}
