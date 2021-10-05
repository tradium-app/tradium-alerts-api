package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.mongodb.client.result.UpdateResult;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.services.ApeWisdomService;
import com.tradiumapp.swingtradealerts.services.models.ApeWisdomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class FetchRedditStocksTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchRedditStocksTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ApeWisdomService apeWisdomService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 20 18 * * 1-5")
    public void fetchAllRedditStocks() throws IOException {
        Response<ApeWisdomResponse> fetchResponse = apeWisdomService.getTopTrendingStocks().execute();
        if (fetchResponse.isSuccessful()) {
            resetRedditRankOfAllStocks();
            List<ApeWisdomResponse.Trending> trendingStocks = fetchResponse.body().results;
            for(ApeWisdomResponse.Trending trending: trendingStocks){
                updateStockRank(trending.ticker, trending.rank);
            }
            logger.info("Trending Reddit Stocks fetched at {}", dateFormat.format(new Date()));
        } else {
            logger.error("Error while fetching reddit stocks: {}", fetchResponse.errorBody().string());
        }
    }

    private boolean resetRedditRankOfAllStocks() {
        Query query = new Query();
        Update update = new Update();
        update.set("redditRank", 0);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Stock.class);
        return result.getModifiedCount() > 100;
    }

    private boolean updateStockRank(String symbol, float redditRank) {
        Query query = new Query();
        query.addCriteria(Criteria.where("symbol").is(symbol));

        Update update = new Update();
        update.set("redditRank", redditRank);
        update.set("modifiedDate", new Date());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Stock.class);
        return result.getModifiedCount() == 1;
    }
}