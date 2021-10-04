package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.services.IexCloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class FetchAllStocksTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchAllStocksTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private IexCloudService iexService;

    @Autowired
    private StockRepository stockRepository;

    @Value("${IEX_API_TOKEN}")
    private String iexToken;

    @Scheduled(cron = "0 0 0 * * 1")
    public void fetchAllStocks() throws IOException {
        Response<List<Stock>> fetchResponse = iexService.listStocks(iexToken).execute();
        if (fetchResponse.isSuccessful()) {
            List<Stock> stocks = fetchResponse.body();
            stockRepository.saveAll(stocks);
            logger.info("stocks fetched: {}", stocks.get(0).company);
        } else {
            logger.error("Error while fetching stocks: {}", fetchResponse.errorBody().string());
        }
        logger.info("The time is now {}", dateFormat.format(new Date()));
    }
}