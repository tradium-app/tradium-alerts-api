
package com.tradiumapp.swingtradealerts.query;

import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.StockHistory;
import com.tradiumapp.swingtradealerts.models.User;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WatchListQuery implements GraphQLQueryResolver {
    @Autowired
    MongoTemplate mongoTemplate;

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).WATCHLIST.id)")
    public List<Stock> getWatchList() {
        String userId = PrincipalManager.getCurrentUserId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(userId));
        User user = mongoTemplate.findOne(query1, User.class);

        if (user.watchList != null) {
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("symbol").in(user.watchList));
            List<Stock> stocks = mongoTemplate.find(query2, Stock.class);

            Query query3 = new Query();
            query3.addCriteria(Criteria.where("symbol").in(user.watchList));
            List<StockHistory> stockHistories = mongoTemplate.find(query3, StockHistory.class);

            long days30Ago = Instant.now().minusSeconds(2_592_000).toEpochMilli();

            for (Stock stock : stocks) {
                stock.last30DaysClosePrices = stockHistories.stream()
                        .filter(h -> h.symbol.equals(stock.symbol))
                        .findFirst().get()
                        .daily_priceHistory.stream()
                        .filter(p -> p.time != null && p.time > days30Ago)
                        .sorted(Comparator.comparing(p -> p.time))
                        .map(p -> p.close)
                        .collect(Collectors.toList());
            }

            Query query4 = new Query();
            query4.addCriteria(Criteria.where("userId").is(userId));
            query4.addCriteria(Criteria.where("symbol").in(user.watchList));
            List<Alert> alerts = mongoTemplate.find(query4, Alert.class);

            for (Stock stock : stocks) {
                stock.isBuyAlert = alerts.stream().anyMatch(a -> a.symbol.equals(stock.symbol)
                        && a.status == Alert.AlertStatus.On
                        && a.signal == Alert.AlertSignal.Buy);

                stock.isSellAlert = alerts.stream().anyMatch(a -> a.symbol.equals(stock.symbol)
                        && a.status == Alert.AlertStatus.On
                        && a.signal == Alert.AlertSignal.Sell);
            }

            return stocks;
        } else {
            return Collections.emptyList();
        }
    }
}
