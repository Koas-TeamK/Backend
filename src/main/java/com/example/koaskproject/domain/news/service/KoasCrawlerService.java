package com.example.koaskproject.domain.news.service;

import com.example.koaskproject.domain.news.dto.NewsSummary;
import com.example.koaskproject.domain.news.entity.News;
import com.example.koaskproject.domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KoasCrawlerService {

    private final String BASE_URL = "https://www.ikoas.com/news/news?page=";
    private final String DETAIL_BASE = "https://www.ikoas.com/news";
    private final Pattern ONCLICK_HREF =
            Pattern.compile("location\\.href\\s*=\\s*'([^']+)'");
    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public List<NewsSummary> getNewsSummary() {
        List<News> newsList = newsRepository.findTop5ByOrderByNumberDesc();

        List<NewsSummary> newsSummaryList=new ArrayList<>();
        newsList.forEach(n->{
            newsSummaryList.add(NewsSummary.builder().id(n.getId()).title(n.getTitle())
                    .thumbnailUrl(n.getImgUrl().isEmpty() ? null : n.getImgUrl().get(0))
                    .date(n.getDate()).build());
        });
        return newsSummaryList;
    }

    public News getNewsDetail(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void crawlNews() throws IOException {
        List<News> allNews = new ArrayList<>();
        int highNumber= Optional.ofNullable(newsRepository.findHighNumber()).orElse(0);
        boolean stop=false;
        for (int page = 1; page <= 100; page++) {
            if(stop)
                break;
            String url = BASE_URL + page;
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            List<String> img;

            Elements rows = doc.select("table.board-list.board-type3 tbody tr.tit");
            if(rows.isEmpty()) break;
            for (Element tr : rows) {



                String numText    = tr.selectFirst("td:nth-child(1)") != null
                        ? tr.selectFirst("td:nth-child(1)").text().trim() : "";


                if(numText.contains("등록된 게시물이 없습니다."))
                {
                    stop=true;
                    break;
                }
                int num = Integer.parseInt(numText);

                if (num <= highNumber) {
                    stop = true;
                    break;
                }

                Element aTag = tr.selectFirst("td:nth-child(2) a");
                String title = aTag != null ? aTag.text().trim() : "";
                String link  = extractRealLink(aTag);


                String date  = tr.selectFirst("td:nth-child(3)") != null
                        ? tr.selectFirst("td:nth-child(3)").text().trim() : "";

                img= fetchDetailImages(link);
                String content = fetchDetailText(link);
                allNews.add(News.builder().number(num).date(date).title(title).content(content).imgUrl(img).build());

            }
        }
        newsRepository.saveAll(allNews);
    }

    private String extractRealLink(Element aTag) {
        if (aTag == null) return "";

        String href = aTag.attr("href");
        if (href != null && !href.isBlank() && !href.equals("#none")) {
            return absolutize(href);
        }
        String onclick = aTag.attr("onclick");
        if (onclick != null && !onclick.isBlank()) {
            Matcher m = ONCLICK_HREF.matcher(onclick);
            if (m.find()) {
                String rel = m.group(1);
                return absolutize(rel);
            }
        }
        return "";
    }
    private String absolutize(String rel) {
        if (rel.startsWith("?"))  return DETAIL_BASE + "/" + rel;
        if (rel.startsWith("/"))  return "https://www.ikoas.com" + rel;
        return DETAIL_BASE + "/" + rel;
    }

    private String fetchDetailText(String link) {
        if (link == null || link.isBlank()) return "";
        try {
            Document doc = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements paragraphs = doc.select("section.board-view p");
            if (paragraphs.isEmpty()) return "";

            return paragraphs.stream()
                    .map(Element::text)
                    .filter(t -> !t.isBlank())
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private List<String> fetchDetailImages(String link) {
        List<String> images = new ArrayList<>();
        if (link == null || link.isBlank()) return images;

        try {
            Document doc = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0")
                    .get();


            Elements imgTags = doc.select("p > img, figure > img");

            for (Element img : imgTags) {
                String imgUrl = img.absUrl("src");
                if (imgUrl.isEmpty()) continue;
                images.add(imgUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return images;
    }

}
