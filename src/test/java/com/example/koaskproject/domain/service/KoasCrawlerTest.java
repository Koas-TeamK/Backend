package com.example.koaskproject.domain.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KoasCrawlerTest {
    private static final String BASE_URL = "https://www.ikoas.com/news/news?page=";
    private static final String DETAIL_BASE = "https://www.ikoas.com/news";
    private static final Pattern ONCLICK_HREF =
            Pattern.compile("location\\.href\\s*=\\s*'([^']+)'");
    static class NewsDto
    {
        String num;
        String title;
        String date;
        String link;
        String content;
        List<String> img;


        public NewsDto(String num, String title, String date, String link, String content, List<String> img) {
            this.num = num;
            this.title = title;
            this.date = date;
            this.link = link;
            this.content = content;
            this.img = img;
        }
    }
    @Test
    public void crawlNews() throws IOException {
            List<NewsDto> allNews = new ArrayList<>();
            for (int page = 1; page <= 10; page++) {
                String url = BASE_URL + page;
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                List<String> img;
                // tbody > tr.tit
                for (Element tr : doc.select("table.board-list.board-type3 tbody tr.tit")) {
                    String num   = tr.selectFirst("td:nth-child(1)") != null
                            ? tr.selectFirst("td:nth-child(1)").text().trim() : "";

                    Element aTag = tr.selectFirst("td:nth-child(2) a");
                    String title = aTag != null ? aTag.text().trim() : "";
                    String link  = extractRealLink(aTag);


                    String date  = tr.selectFirst("td:nth-child(3)") != null
                            ? tr.selectFirst("td:nth-child(3)").text().trim() : "";

                    img= fetchDetailImages(link);
                    String content = fetchDetailText(link);
                    allNews.add(new NewsDto(num, title, date, link, content,img));
                }
            }
            for(NewsDto newsDto : allNews) {
                System.out.println(newsDto.num + " " + newsDto.title + " " + newsDto.date);
                System.out.println(newsDto.link + " " + newsDto.content);
                for(String imgUrl : newsDto.img) {
                    System.out.println("url:" +imgUrl);
                }
            }

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

