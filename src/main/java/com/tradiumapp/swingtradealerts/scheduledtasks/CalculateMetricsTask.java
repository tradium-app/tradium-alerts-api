package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.StockHistory;
import com.tradiumapp.swingtradealerts.repositories.StockHistoryRepository;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CalculateMetricsTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchAllStocksTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Scheduled(cron = "0 0 */4 * * *", zone = "EST")
    public void calculateMetrics() throws IOException {
        long startEpoch = Instant.now().minusSeconds(2_592_000).toEpochMilli();
        List<Stock> updatedStocks = new ArrayList<>();

        Iterable<StockHistory> stockHistories = stockHistoryRepository.findAll();
        List<Stock> stocks = (List<Stock>) stockRepository.findAll();

        for (StockHistory history : stockHistories) {
            if (history.daily_priceHistory == null) continue;
            try {
                List<StockHistory.StockPrice> stockPrices = history.daily_priceHistory.stream()
                        .filter(stockPrice -> stockPrice.time != null && stockPrice.time > startEpoch)
                        .collect(Collectors.toList());

                BarSeries series = new BaseBarSeriesBuilder().build();

                for (StockHistory.StockPrice stockPrice : stockPrices) {
                    Instant instant = Instant.ofEpochSecond(stockPrice.time);
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
                    try {
                        series.addBar(zonedDateTime, stockPrice.open, stockPrice.high, stockPrice.low, stockPrice.close, stockPrice.volume);
                    } catch (Exception ignore) {
                    }
                }

                PriceIndicator priceIndicator = new ClosePriceIndicator(series);
                RSIIndicator rsiIndicator = new RSIIndicator(priceIndicator, 14);
                float rsiValue = rsiIndicator.getValue(rsiIndicator.getBarSeries().getBarCount() - 1).floatValue();

                EMAIndicator emaIndicator = new EMAIndicator(priceIndicator, 20);
                float emaValue = emaIndicator.getValue(emaIndicator.getBarSeries().getBarCount() - 1).floatValue();

                Stock stock = stocks.stream().filter(s -> s.symbol.equals(history.symbol)).findFirst().get();
                stock.rsi = rsiValue;
                stock.trend = stock.price > emaValue ? Stock.StockTrend.Up : Stock.StockTrend.Down;
                updatedStocks.add(stock);
            } catch (Exception ex) {
                logger.error("Error calculating metrics for {}: {}", history.symbol, ex);
            }
        }

        stockRepository.saveAll(updatedStocks);
        logger.info("CalculateMetricsTask ran at {}", dateFormat.format(new Date()));
    }
}