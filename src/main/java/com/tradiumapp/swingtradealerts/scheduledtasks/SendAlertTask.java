package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.AlertRepository;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        List<Alert> alerts = (List<Alert>) alertRepository.findAll();
        List<User> users = (List<User>) userRepository.findAll();
        HashMap<String, List<Stock.StockPrice>> stockPricesMap = new HashMap<>();

        for (Alert alert : alerts) {
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("symbol").is(alert.symbol));
            Stock stock = mongoTemplate.findOne(query1, Stock.class);
            stockPricesMap.put(stock.symbol, stock.daily_priceHistory);
        }

        for (Alert alert : alerts) {
            BarSeries series = new BaseBarSeriesBuilder().withName(alert.symbol).build();
            List<Stock.StockPrice> stockPrices = stockPricesMap.get(alert.symbol);
            stockPrices.sort(Comparator.comparing((Stock.StockPrice o) -> o.time));
            stockPrices.removeIf((Stock.StockPrice s) -> s.time.equals(0L));

            for (Stock.StockPrice stockPrice : stockPrices) {
                Instant instant = Instant.ofEpochSecond(stockPrice.time);
                ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
                series.addBar(zonedDateTime, stockPrice.open, stockPrice.high, stockPrice.low, stockPrice.close, stockPrice.volume);
            }

            ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
            RSIIndicator rsiIndicator= new RSIIndicator(closePrice, 14);
            float lastRsiValue = rsiIndicator.getValue(rsiIndicator.getBarSeries().getBarCount()-1).floatValue();
            if(lastRsiValue > 40){
                sendEmail(users.get(0), alert.symbol + " RSI Trigger", "RSI " + lastRsiValue + " greater than 40");
            }
        }

        logger.info("The time is now {}", dateFormat.format(new Date()));
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