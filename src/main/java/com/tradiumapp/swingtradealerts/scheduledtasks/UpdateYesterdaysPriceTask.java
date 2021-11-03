package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.jobscheduler.QuartzJob;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.StockHistory;
import com.tradiumapp.swingtradealerts.repositories.StockHistoryRepository;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@QuartzJob
public class UpdateYesterdaysPriceTask implements Job {
    private static final Logger logger = LoggerFactory.getLogger(UpdateYesterdaysPriceTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long yesterdayStartEpoch = Instant.now().minusSeconds(86400).toEpochMilli();
        List<Stock> updatedStocks = new ArrayList<>();

        Iterable<StockHistory> stockHistories = stockHistoryRepository.findAll();
        List<Stock> stocks = (List<Stock>) stockRepository.findAll();

        for (StockHistory history : stockHistories) {
            try {
                if (history.daily_priceHistory == null || history.daily_priceHistory.size() < 30) continue;
                Optional<Stock> stockOptional = stocks.stream().filter(s -> s.symbol.equals(history.symbol)).findFirst();
                if (stockOptional.isPresent()) {
                    stockOptional.get().yesterdaysPrice = history.daily_priceHistory.stream()
                            .filter(sp -> sp.time != null && sp.time > yesterdayStartEpoch).findFirst().get().close;
                    updatedStocks.add(stockOptional.get());
                }
            } catch (Exception ex) {
                logger.error("Error updating Yesterdays price for {}: {}", history.symbol, ex);
            }
        }

        stockRepository.saveAll(updatedStocks);
        logger.info("UpdateYesterdaysPriceTask ran at {}", dateFormat.format(new Date()));
    }
}