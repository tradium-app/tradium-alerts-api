package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import com.tradiumapp.swingtradealerts.services.TipranksService;
import com.tradiumapp.swingtradealerts.services.models.TipranksDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Component
public class TipranksMetricsTask {
    private static final Logger logger = LoggerFactory.getLogger(SendAlertTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private TipranksService tipranksService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Scheduled(cron = "0 0 11 * * *", zone = "EST")
    public void getData() throws IOException {
        try {
            List<User> users = (List<User>) userRepository.findAll();
            Set<String> symbols = new HashSet<>();

            for (User user : users) {
                if (user.watchList != null) symbols.addAll(user.watchList);
            }

            List<Stock> stocks = stockRepository.findBySymbolIn(new ArrayList<>(symbols));
            Collections.shuffle(stocks);
            List<Stock> updatedStocks = new ArrayList<>();

            long epochTime = Instant.now().toEpochMilli();

            for (Stock stock : stocks) {
                try {
                    Response<TipranksDataResponse> response = tipranksService.getData(stock.symbol, String.valueOf(epochTime)).execute();

                    if (response.isSuccessful()) {
                        stock.company = response.body().companyName;
                        stock.sector = response.body().companyData.sector;
                        stock.industry = response.body().companyData.industry;

                        stock.tipranksPriceTarget = response.body().ptConsensus.stream().filter(p -> p.period == 0).findFirst().get().priceTarget;
                    } else {
                        logger.error("Tipranks api failed: {}", response.errorBody());
                    }

                    updatedStocks.add(stock);
                } catch (Exception ex) {
                    logger.error("Error while pulling TipRanks for stock {}", stock.symbol, ex);
                }

                Thread.sleep(30000);
            }

            stockRepository.saveAll(updatedStocks);
            logger.info("TipranksMetricsTask ran at {}", dateFormat.format(new Date()));
        } catch (Exception ex) {
            logger.error("Error while running TipranksMetricsTask: ", ex);
        }
    }
}
