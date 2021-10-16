package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.mongodb.client.result.UpdateResult;
import com.tradiumapp.swingtradealerts.models.*;
import com.tradiumapp.swingtradealerts.repositories.AlertRepository;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers.*;
import com.tradiumapp.swingtradealerts.scheduledtasks.helpers.AlertEmailSender;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SendAlertTask {
    private static final Logger logger = LoggerFactory.getLogger(SendAlertTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AlertEmailSender alertEmailSender;

    @Scheduled(cron = "0 0 11,19 * * *", zone = "EST")
    public void sendAlerts() throws IOException {
        List<Alert> alerts = alertRepository.findByEnabled(true);
        alerts = alerts.stream().filter(a -> a.id.equals(new ObjectId("616ac9231294775efa1d877c"))).collect(Collectors.toList());
        HashMap<String, List<StockHistory.StockPrice>> stockPricesMap = loadStockHistory(alerts);

        HashMap<String, Stock> stocksMap = new HashMap<>();
        for (String symbol : stockPricesMap.keySet()) {
            stocksMap.put(symbol, stockRepository.findBySymbol(symbol));
        }

        List<Alert> alertsToBeFired = new ArrayList<>();

        for (Alert alert : alerts) {
            try {
                BarSeries series = new BaseBarSeriesBuilder().withName(alert.symbol).build();
                List<StockHistory.StockPrice> stockPrices = stockPricesMap.get(alert.symbol);
                if (stockPrices == null) break;
                stockPrices.sort(Comparator.comparing((StockHistory.StockPrice o) -> o.time));
                stockPrices.removeIf((StockHistory.StockPrice s) -> s.time.equals(0L));

                for (StockHistory.StockPrice stockPrice : stockPrices) {
                    Instant instant = Instant.ofEpochSecond(stockPrice.time);
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
                    try {
                        series.addBar(zonedDateTime, stockPrice.open, stockPrice.high, stockPrice.low, stockPrice.close, stockPrice.volume);
                    } catch (Exception ignore) {
                    }
                }

                boolean shouldAlertFire = true;
                for (Condition condition : alert.conditions) {
                    shouldAlertFire = shouldAlertFire && isConditionMet(stocksMap.get(alert.symbol), condition, series);
                }

                Alert.AlertStatus originalStatus = alert.status;
                if (shouldAlertFire) {
                    if (alert.status == Alert.AlertStatus.Off) {
                        alertsToBeFired.add(alert);
                        alert.status = Alert.AlertStatus.On;
                    }
                } else {
                    alert.status = Alert.AlertStatus.Off;
                }
                if (originalStatus != alert.status) updateAlertStatus(alert, alert.status);
            } catch (Exception ex) {
                logger.error("Error while checking alert id {}: ", alert.id, ex);
            }
        }

        Set<String> userIdsToBeAlerted = alertsToBeFired.stream().map(a -> a.userId).collect(Collectors.toSet());
        List<User> users = (List<User>) userRepository.findAll();

        for (String userId : userIdsToBeAlerted) {
            User user = users.stream().filter(u -> u.id.toString().equals(userId)).findFirst().get();
            List<Alert> userAlerts = alertsToBeFired.stream().filter(a -> a.userId.equals(userId)).collect(Collectors.toList());
            alertEmailSender.sendEmail(user, userAlerts);
        }

        logger.info("SendAlertTask ran at {}", dateFormat.format(new Date()));
    }

    @NotNull
    private HashMap<String, List<StockHistory.StockPrice>> loadStockHistory(List<Alert> alerts) {
        HashMap<String, List<StockHistory.StockPrice>> stockPricesMap = new HashMap<>();
        long startEpoch = Instant.now().minusSeconds(5_184_000).toEpochMilli();

        for (Alert alert : alerts) {
            if (!stockPricesMap.containsKey(alert.symbol)) {
                Query query1 = new Query();
                Criteria criteria = new Criteria().andOperator(Criteria.where("symbol").is(alert.symbol),
                        Criteria.where("daily_priceHistory.time").gt(startEpoch));
                query1.addCriteria(criteria);
                StockHistory stock = mongoTemplate.findOne(query1, StockHistory.class);
                if (stock != null) {
                    List stockPrices = stock.daily_priceHistory.stream()
                            .filter(stockPrice -> stockPrice.time != null && stockPrice.time > startEpoch)
                            .collect(Collectors.toList());
                    stockPricesMap.put(stock.symbol, stockPrices);
                }
            }
        }
        return stockPricesMap;
    }

    private boolean isConditionMet(Stock stock, Condition condition, BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        ConditionChecker conditionChecker;
        if (condition.indicator.equals(IndicatorType.rsi)) {
            conditionChecker = new RSIConditionChecker();
        } else if (condition.indicator.equals(IndicatorType.sma)) {
            conditionChecker = new SMAConditionChecker();
        } else if (condition.indicator.equals(IndicatorType.ema)) {
            conditionChecker = new EMAConditionChecker();
        } else if (condition.indicator.equals(IndicatorType.week52high)) {
            conditionChecker = new Week52HighConditionChecker(stock);
        } else if (condition.indicator.equals(IndicatorType.week52low)) {
            conditionChecker = new Week52LowConditionChecker(stock);
        } else if (condition.indicator.equals(IndicatorType.earnings)) {
            conditionChecker = new EarningsConditionChecker(stock);
        } else {
            conditionChecker = new RedditTrendingConditionChecker(stock);
        }

        boolean result = conditionChecker.checkCondition(condition, closePrice);

        return (condition.operator == Condition.Operator.Not) ? !result : result;
    }

    private boolean updateAlertStatus(Alert alert, Alert.AlertStatus status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(alert.id));

        Update update = new Update();
        update.set("status", status);
        if (status == Alert.AlertStatus.On) update.set("alertOnDate", new Date());
        update.set("modifiedDate", new Date());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Alert.class);
        return result.getModifiedCount() == 1;
    }


}