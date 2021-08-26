package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.services.PolygonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FetchQuotesTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchQuotesTask.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private PolygonService polygonService;

    @Value("${POLYGON_API_KEY}")
    private String apiKey;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 18 * * 1-5")
    public void fetchQuotes() throws IOException {
        String day = dayFormat.format(new Date());
        Response<PolygonQuoteResponse> response = polygonService.getQuotes(day, apiKey).execute();

        List<Stock> stocks = (List<Stock>) stockRepository.findAll();

        List<Stock> updatedStocks = new ArrayList<>();

        response.body().results.forEach((stockPrice) -> {
                Optional<Stock> stockOptional = stocks.stream().filter(s -> s.symbol.equals(stockPrice.symbol)).findFirst();
                if(!stockOptional.isPresent()) return;
                Stock stock = stockOptional.get();
                if(stock.daily_priceHistory == null) stock.daily_priceHistory = new ArrayList<>();
                stock.daily_priceHistory.add(stockPrice);
                updatedStocks.add(stock);
        });

        stockRepository.saveAll(updatedStocks);

        logger.info("FetchQuotesTask ran at {}", timeFormat.format(new Date()));
    }
}