package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FetchQuotesTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchQuotesTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private IexCloudService iexService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${IEX_API_TOKEN}")
    private String iexToken;

    @Scheduled(cron = "0 0 * * * *")
    public void fetchQuotes() throws IOException {
        Query query = new Query();
        query.addCriteria(Criteria.where("shouldRefresh").is(true));
        query.limit(10);
        List<Stock> stocks = mongoTemplate.find(query, Stock.class);
        List<String> symbols = stocks.stream().map(s -> s.symbol).collect(Collectors.toList());

        if (symbols.size() > 0) {
            String symbolJoined = String.join(",", symbols);
            Response<HashMap<String, HashMap<String, Stock>>> response = iexService.getQuotes(symbolJoined, iexToken).execute();

            if(!response.isSuccessful()){
                logger.error("Error while fetching stocks: {}", response.errorBody().toString());
            }

            List<Stock> updatedStocks = new ArrayList<>();

            response.body().forEach((key, quote) -> {
                Stock updatedStock = quote.get("quote");
                updatedStock.isEnabled = true;
                updatedStock.shouldRefresh = true;
                updatedStock.id = stocks.stream()
                        .filter(s -> s.symbol.equals(updatedStock.symbol))
                        .map(s -> s.id).findAny().get();
                updatedStocks.add(quote.get("quote"));
            });

            stockRepository.saveAll(updatedStocks);

            logger.info("FetchQuotesTask ran at {}", dateFormat.format(new Date()));
        }
    }
}