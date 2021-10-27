package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.StockHistory;
import com.tradiumapp.swingtradealerts.repositories.StockHistoryRepository;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.services.PolygonService;
import com.tradiumapp.swingtradealerts.services.models.PolygonQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private StockRepository stockRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Scheduled(cron = "0 0 18 * * 1-5", zone = "EST")
    public void fetchQuotes() throws IOException {
        try {
            String day = dayFormat.format(new Date());
            Response<PolygonQuoteResponse> response = polygonService.getQuotes(day, apiKey).execute();

            List<Stock> stocks = (List<Stock>) stockRepository.findAll();
            List<StockHistory> stockHistories = (List<StockHistory>) stockHistoryRepository.findAll();

            List<Stock> updatedStocks = new ArrayList<>();
            List<StockHistory> updatedStockHistories = new ArrayList<>();

            if (response.isSuccessful() && response.body() != null && response.body().results != null) {
                response.body().results.forEach((stockPrice) -> {
                    Optional<Stock> stockOptional = stocks.stream().filter(s -> s.symbol.equals(stockPrice.symbol)).findFirst();
                    Optional<StockHistory> stockHistoryOptional = stockHistories.stream().filter(s -> s.symbol.equals(stockPrice.symbol)).findFirst();

                    Stock stock = stockOptional.orElseGet(Stock::new);
                    StockHistory stockHistory = stockHistoryOptional.orElseGet(StockHistory::new);

                    stock.symbol = stockPrice.symbol;
                    if (stock.price > 0) stock.changePercent = (stockPrice.close - stock.price) * 100 / stock.price;
                    stock.price = stockPrice.close;
                    updatedStocks.add(stock);

                    stockHistory.symbol = stockPrice.symbol;
                    if (stockHistory.daily_priceHistory == null) stockHistory.daily_priceHistory = new ArrayList<>();
                    stockHistory.daily_priceHistory.add(stockPrice);
                    updatedStockHistories.add(stockHistory);
                });

                stockRepository.saveAll(updatedStocks);
                stockHistoryRepository.saveAll(updatedStockHistories);

                logger.info("FetchQuotesTask ran at {}", timeFormat.format(new Date()));
            } else {
                logger.error("Error response from Polygon Api for day {}. {}", day, response);
            }
        } catch (Exception ex) {
            logger.error("Error while running FetchQuotesTask. ", ex);
        }
    }
}