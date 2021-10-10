package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Article;
import com.tradiumapp.swingtradealerts.repositories.ArticleRepository;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SaNewsParserTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchRedditStocksTask.class);

    @Autowired
    private ArticleRepository articleRepository;

    private List<String> SaUrls = Arrays.asList("https://seekingalpha.com/market-news/top-news",
            "https://seekingalpha.com/market-news/all", "https://seekingalpha.com/market-news/technology");

    @Scheduled(cron = "0 20 18 * * 1-5", zone = "EST")
    public void fetchSaTopNews() throws IOException {
        List<Article> articles = new ArrayList<>();

        for (String saUrl : SaUrls) {
            Document doc;
            try {
                doc = Jsoup.connect(saUrl).get();
            } catch (HttpStatusException ex) {
                logger.error("Error while fetching seeking-alpha top news", ex);
                return;
            }

            Elements newsList = doc.select(".mc-list > li[id^=\"premium-news\"]");

            for (Element newsElement : newsList) {
                Element tickerElement = newsElement.select(".media-left a").first();

                if (tickerElement != null) {
                    String articleSymbol = tickerElement.text();
                    if (!articleSymbol.contains(":") && !articleSymbol.equals("SPY")) {
                        Element headlineElement = newsElement.select(".media-body .title a").first();
                        String articleHeadline = headlineElement.text();
                        String articleLink = headlineElement.attributes().get("href");

                        Article article = new Article();
                        article.symbol = articleSymbol.toUpperCase();
                        article.headline = articleHeadline;
                        article.link = "https://seekingalpha.com" + articleLink;
                        articles.add(article);
                    }
                }
            }
        }

        try {
            articleRepository.saveAll(articles);
        } catch (Exception ignored) {
        }
    }
}