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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Scheduled(cron = "0 0 0 * * 1", zone = "EST")
    public void fetchAllStocks() {
        try {
            Response<List<Stock>> fetchResponse = iexService.listStocks(iexToken).execute();
            if (fetchResponse.isSuccessful()) {
                List<Stock> allStocks = fetchResponse.body();
                List<Stock> savedStocks = (List<Stock>) stockRepository.findAll();

                List<Stock> newStocks = allStocks.stream().filter(s -> !savedStocks.stream().anyMatch(ss -> ss.symbol.equals(s.symbol))).collect(Collectors.toList());

                stockRepository.saveAll(newStocks);
                logger.info("stocks fetched: {}", allStocks.get(0).company);
            } else {
                logger.error("Error while fetching stocks: {}", fetchResponse.errorBody().string());
            }

            logger.info("FetchAllStocksTask ran at {}", dateFormat.format(new Date()));
        } catch (Exception ex) {
            logger.error("Error running FetchAllStocksTask: ", ex);
        }

    }
}