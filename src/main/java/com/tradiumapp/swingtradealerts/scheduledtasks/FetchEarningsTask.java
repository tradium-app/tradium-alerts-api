package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.services.IexCloudService;
import com.tradiumapp.swingtradealerts.services.models.IexcloudEarningsResponse;
import org.joda.time.DateTime;
import org.joda.time.Days;
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
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class FetchEarningsTask {
    private static final Logger logger = LoggerFactory.getLogger(FetchEarningsTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
    private static final SimpleDateFormat iexDateFormat = new SimpleDateFormat("YYYYMMdd");

    @Autowired
    private IexCloudService iexService;

    @Value("${IEX_API_TOKEN}")
    private String iexToken;

    @Autowired
    MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 0 */10 * *", zone = "EST")
    public void fetchUpcomingEarnings() {
        try {
            String today = iexDateFormat.format(new Date());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 15);
            String endDate = iexDateFormat.format(cal.getTime());

            Response<List<IexcloudEarningsResponse>> iexResponse = iexService.fetchUpcomingEarnings(today, endDate, iexToken).execute();
            if (iexResponse.isSuccessful()) {
                List<IexcloudEarningsResponse> earningsResponses = iexResponse.body();

                for (IexcloudEarningsResponse iexEarning : earningsResponses) {
                    DateTime reportDate = DateTime.parse(iexEarning.reportDate);

                    if (Days.daysBetween(DateTime.now(), reportDate).getDays() > 0) {
                        Query query = new Query();
                        query.addCriteria(Criteria.where("symbol").is(iexEarning.symbol));

                        Update update = new Update();
                        update.set("nextEarningsDate", reportDate);

                        mongoTemplate.updateFirst(query, update, Stock.class);
                    }
                }
            } else {
                logger.error("Error while fetching stocks: {}", iexResponse.errorBody().string());
            }
            logger.info("FetchEarningsTask ran at {}", dateFormat.format(new Date()));
        } catch (Exception ex) {
            logger.error("Error while running FetchEarningsTask", ex);
        }
    }
}