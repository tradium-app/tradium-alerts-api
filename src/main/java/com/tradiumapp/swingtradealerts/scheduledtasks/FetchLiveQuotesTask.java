package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.scheduledtasks.utilities.StockUtility;
import com.tradiumapp.swingtradealerts.services.IexCloudService;
import com.tradiumapp.swingtradealerts.services.models.IexNewsQuoteResponse;
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
import java.util.stream.Collectors;

@Component
public class FetchLiveQuotesTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchLiveQuotesTask.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private IexCloudService iexService;

    @Value("${IEX_API_TOKEN}")
    private String iexToken;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockUtility stockUtility;

    @Scheduled(cron = "0 */30 9-17 * * 1-5", zone = "EST")
    public void fetchLiveQuotesTask() throws IOException {
        try {
            String day = dayFormat.format(new Date());

            List<String> symbolsInWatchList = stockUtility.fetchStocksInWatchList().stream().map(s -> s.symbol).collect(Collectors.toList());
            Response<HashMap<String, IexNewsQuoteResponse>> response = iexService.fetchQuotes(String.join(",", symbolsInWatchList), iexToken).execute();

            List<Stock> stocks = stockRepository.findBySymbolIn(symbolsInWatchList);
            List<Stock> updatedStocks = new ArrayList<>();

            if (response.isSuccessful() && response.body() != null) {
                for (Map.Entry<String, IexNewsQuoteResponse> entry : response.body().entrySet()) {
                    Stock stock = stocks.stream().filter(s -> s.symbol.equals(entry.getKey())).findFirst().get();
                    stock.price = entry.getValue().quote.latestPrice;
                    stock.company = entry.getValue().quote.companyName;
                    stock.peRatio = entry.getValue().quote.peRatio;
                    stock.week52High = entry.getValue().quote.week52High;
                    stock.week52Low = entry.getValue().quote.week52Low;

                    updatedStocks.add(stock);
                }

                stockRepository.saveAll(updatedStocks);

                logger.info("FetchLiveQuotesTask ran at {}", timeFormat.format(new Date()));
            } else {
                logger.error("Error response from IexCloud Api for day {}. {}", day, mapper.writeValueAsString(response.body()));
            }
        } catch (Exception ex) {
            logger.error("Error while running FetchLiveQuotesTask. ", ex);
        }
    }
}