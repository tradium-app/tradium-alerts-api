package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.mongodb.client.result.UpdateResult;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tradiumapp.swingtradealerts.models.*;
import com.tradiumapp.swingtradealerts.repositories.AlertRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.RSIIndicator;
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
    MongoTemplate mongoTemplate;

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Scheduled(cron = "0 0 0 */1 * *")
    public void sendAlerts() throws IOException {
        List<Alert> alerts = alertRepository.findByStatusNot(AlertStatus.Disabled);
        List<User> users = (List<User>) userRepository.findAll();
        HashMap<String, List<Stock.StockPrice>> stockPricesMap = new HashMap<>();
        long startEpoch = Instant.now().minusSeconds(2_592_000).toEpochMilli();

        for (Alert alert : alerts) {
            if (!stockPricesMap.containsKey(alert.symbol)) {
                Query query1 = new Query();
                Criteria criteria = new Criteria().andOperator(Criteria.where("symbol").is(alert.symbol),
                        Criteria.where("daily_priceHistory.time").gt(startEpoch));
                query1.addCriteria(criteria);
                Stock stock = mongoTemplate.findOne(query1, Stock.class);
                if (stock != null) {
                    List stockPrices = stock.daily_priceHistory.stream()
                            .filter(stockPrice -> stockPrice.time != null && stockPrice.time > startEpoch)
                            .collect(Collectors.toList());
                    stockPricesMap.put(stock.symbol, stockPrices);
                }
            }
        }

        for (Alert alert : alerts) {
            BarSeries series = new BaseBarSeriesBuilder().withName(alert.symbol).build();
            List<Stock.StockPrice> stockPrices = stockPricesMap.get(alert.symbol);
            if (stockPrices == null) break;
            stockPrices.sort(Comparator.comparing((Stock.StockPrice o) -> o.time));
            stockPrices.removeIf((Stock.StockPrice s) -> s.time.equals(0L));

            for (Stock.StockPrice stockPrice : stockPrices) {
                Instant instant = Instant.ofEpochSecond(stockPrice.time);
                ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
                series.addBar(zonedDateTime, stockPrice.open, stockPrice.high, stockPrice.low, stockPrice.close, stockPrice.volume);
            }

            boolean shouldAlertFire = true;
            for (Condition condition : alert.conditions) {
                shouldAlertFire = shouldAlertFire && isConditionMet(condition, series);
            }

            if (shouldAlertFire) {
                if (alert.status == AlertStatus.Off) {
                    sendEmail(users.get(0), alert.title, "Alert conditions met.");
                    alert.status = AlertStatus.On;
                }
            } else {
                alert.status = AlertStatus.Off;
            }
            updateAlert(alert, alert.status);
        }

        logger.info("The time is now {}", dateFormat.format(new Date()));
    }

    private boolean isConditionMet(Condition condition, BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsiIndicator = new RSIIndicator(closePrice, condition.valueConfig.length);
        float lastRsiValue = rsiIndicator.getValue(rsiIndicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.valueConfig.upDirection && lastRsiValue > condition.valueConfig.value) {
            return true;
        }
        if (!condition.valueConfig.upDirection && lastRsiValue < condition.valueConfig.value) {
            return true;
        } else {
            return false;
        }
    }

    private boolean updateAlert(Alert alert, AlertStatus status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(alert.id));

        Update update = new Update();
        update.set("status", status);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Alert.class);
        return result.getModifiedCount() == 1;
    }

    private void sendEmail(User user, String subject, String message) throws IOException {
        Email from = new Email("info@tradiumapp.com");
        Email to = new Email(user.email);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }
}