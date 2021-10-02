package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import com.tradiumapp.swingtradealerts.services.FinnhubService;
import com.tradiumapp.swingtradealerts.services.models.FinnhubMetricResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FetchStockMetricsTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchStockMetricsTask.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private FinnhubService finnhubService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Value("${FINNHUB_API_KEY}")
    private String apiKey;

    @Scheduled(cron = "0 0 18 * * 1-5")
    public void fetchStockMetrics() throws IOException {
        List<User> users = (List<User>) userRepository.findAll();
        Set<String> symbols = new HashSet<>();

        for (User user : users) {
            if (user.watchList != null) symbols.addAll(user.watchList);
        }

        List<Stock> stocks = stockRepository.findBySymbolIn(new ArrayList<>(symbols));
        List<Stock> updatedStocks = new ArrayList<>();

        for (Stock stock : stocks) {
            Response<FinnhubMetricResponse> fetchResponse = finnhubService.getStockMetrics(stock.symbol, apiKey).execute();

            stock.beta = fetchResponse.body().metric.beta;
            stock.marketCap = fetchResponse.body().metric.marketCapitalization;
            stock.week52High = fetchResponse.body().metric._52WeekHigh;
            stock.week52Low = fetchResponse.body().metric._52WeekLow;
            stock.revenueGrowthQuarterlyYoy = fetchResponse.body().metric.revenueGrowthQuarterlyYoy;
            stock.revenueGrowthTTMYoy = fetchResponse.body().metric.revenueGrowthTTMYoy;

            updatedStocks.add(stock);
        }

        stockRepository.saveAll(updatedStocks);
        logger.info("FetchStockMetricsTask ran at {}", timeFormat.format(new Date()));
    }
}
