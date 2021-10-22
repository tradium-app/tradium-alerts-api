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

    @Scheduled(cron = "0 40 18 * * 1-5", zone = "EST")
    public void fetchStockMetrics() {
        try {
            List<User> users = (List<User>) userRepository.findAll();
            Set<String> symbols = new HashSet<>();

            for (User user : users) {
                if (user.watchList != null) symbols.addAll(user.watchList);
            }

            List<Stock> stocks = stockRepository.findBySymbolIn(new ArrayList<>(symbols));
            Collections.shuffle(stocks);
            List<Stock> updatedStocks = new ArrayList<>();

            for (Stock stock : stocks) {
                Response<FinnhubMetricResponse> fetchResponse = finnhubService.getStockMetrics(stock.symbol, apiKey).execute();

                FinnhubMetricResponse response = fetchResponse.body();
                if(response.metric != null) {
                    stock.beta = response.metric.beta;
                    stock.marketCap = response.metric.marketCapitalization;
                    stock.week52High = response.metric._52WeekHigh;
                    stock.week52Low = response.metric._52WeekLow;
                    stock.revenueGrowthQuarterlyYoy = response.metric.revenueGrowthQuarterlyYoy;
                    stock.revenueGrowthTTMYoy = response.metric.revenueGrowthTTMYoy;
                }

                FinnhubMetricResponse.Series.Quarterly quarterly = response.series.quarterly;
                if (quarterly != null && quarterly.grossMargin != null) {
                    if (quarterly.grossMargin.size() > 0) stock.grossMargin = quarterly.grossMargin.get(0).v;
                    if (quarterly.salesPerShare.size() > 0) stock.salesPerShareTTM = quarterly.salesPerShare.stream()
                            .sorted(Comparator.comparing(m -> m.period, Comparator.reverseOrder()))
                            .limit(4)
                            .map(s -> s.v)
                            .reduce(0F, Float::sum);
                    if (quarterly.salesPerShare.size() > 0) stock.earningsPerShareTTM = quarterly.eps.stream()
                            .sorted(Comparator.comparing(m -> m.period, Comparator.reverseOrder()))
                            .limit(4)
                            .map(s -> s.v)
                            .reduce(0F, Float::sum);
                }


                updatedStocks.add(stock);
            }

            stockRepository.saveAll(updatedStocks);
            logger.info("FetchStockMetricsTask ran at {}", timeFormat.format(new Date()));
        } catch (Exception ex) {
            logger.error("Error while running FetchStockMetricsTask: ", ex);
        }
    }
}
