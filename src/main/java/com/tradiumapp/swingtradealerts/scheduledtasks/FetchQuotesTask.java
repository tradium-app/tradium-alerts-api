package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.StockHistory;
import com.tradiumapp.swingtradealerts.repositories.StockHistoryRepository;
import com.tradiumapp.swingtradealerts.services.PolygonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private StockHistoryRepository stockHistoryRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 18 * * 1-5")
    public void fetchQuotes() throws IOException {
        String day = dayFormat.format(new Date());
        Response<PolygonQuoteResponse> response = polygonService.getQuotes(day, apiKey).execute();

        List<StockHistory> stocks = (List<StockHistory>) stockHistoryRepository.findAll();
        List<StockHistory> updatedStocks = new ArrayList<>();

        assert response.body().results != null;
        response.body().results.forEach((stockPrice) -> {
            Optional<StockHistory> stockOptional = stocks.stream().filter(s -> s.symbol.equals(stockPrice.symbol)).findFirst();

            StockHistory stock = stockOptional.orElseGet(StockHistory::new);
            stock.symbol = stockPrice.symbol;

            if(stock.daily_priceHistory == null) stock.daily_priceHistory = new ArrayList<>();
            stock.daily_priceHistory.add(stockPrice);
            updatedStocks.add(stock);
        });

        stockHistoryRepository.saveAll(updatedStocks);

        logger.info("FetchQuotesTask ran at {}", timeFormat.format(new Date()));
    }
}