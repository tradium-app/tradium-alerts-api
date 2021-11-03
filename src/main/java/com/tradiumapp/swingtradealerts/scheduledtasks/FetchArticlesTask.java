package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradiumapp.swingtradealerts.models.Article;
import com.tradiumapp.swingtradealerts.scheduledtasks.utilities.StockUtility;
import com.tradiumapp.swingtradealerts.services.IexCloudService;
import com.tradiumapp.swingtradealerts.services.models.IexNewsQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FetchArticlesTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchLiveQuotesTask.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private IexCloudService iexService;

    @Value("${IEX_API_TOKEN}")
    private String iexToken;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private StockUtility stockUtility;

    @Scheduled(cron = "0 0 */4 * * *", zone = "EST")
    public void fetchArticles() throws IOException {
        try {
            String day = dayFormat.format(new Date());

            List<String> symbolsInWatchList = stockUtility.fetchStocksInWatchList().stream().map(s -> s.symbol).collect(Collectors.toList());
            Response<HashMap<String, IexNewsQuoteResponse>> response = iexService.fetchNews(String.join(",", symbolsInWatchList), iexToken).execute();

            List<Article> articles = new ArrayList<>();

            if (response.isSuccessful() && response.body() != null) {
                for (Map.Entry<String, IexNewsQuoteResponse> entry : response.body().entrySet()) {
                    for (IexNewsQuoteResponse.News news : entry.getValue().news) {
                        Article article = new Article();
                        article.symbol = entry.getKey();
                        article.headline = news.headline;
                        article.link = news.url;
                        article.summary = news.summary;

                        articles.add(article);
                    }
                }

                try {
                    mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Article.class)
                            .insert(articles)
                            .execute();
                } catch (Exception ex) {
                    if (!(ex instanceof BulkOperationException)) {
                        logger.error("Erro while running SaNewsParserTask: ", ex);
                    }
                }

                logger.info("FetchQuotesTask ran at {}", timeFormat.format(new Date()));
            } else {
                logger.error("Error response from Polygon Api for day {}. {}", day, mapper.writeValueAsString(response.body()));
            }
        } catch (Exception ex) {
            logger.error("Error while running FetchQuotesTask. ", ex);
        }
    }
}