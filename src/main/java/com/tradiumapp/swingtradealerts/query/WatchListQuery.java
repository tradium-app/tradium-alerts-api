
package com.tradiumapp.swingtradealerts.query;

import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Article;
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
import java.util.ArrayList;
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

            return stocks;
        } else {
            return Collections.emptyList();
        }
    }

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).WATCHLIST.id)")
    public List<Stock> getWatchListStockTrendlines() {
        String userId = PrincipalManager.getCurrentUserId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(userId));
        User user = mongoTemplate.findOne(query1, User.class);

        if (user.watchList != null) {
            List<Stock> stocks = new ArrayList<Stock>();

            Query query3 = new Query();
            query3.addCriteria(Criteria.where("symbol").in(user.watchList));
            List<StockHistory> stockHistories = mongoTemplate.find(query3, StockHistory.class);

            long days30Ago = Instant.now().minusSeconds(2_592_000).toEpochMilli();

            for (StockHistory stockHistory : stockHistories) {
                Stock stock = new Stock() {{
                    symbol = stockHistory.symbol;
                }};
                stock.last30DaysClosePrices = stockHistories.stream()
                        .filter(h -> h.symbol.equals(stock.symbol))
                        .findFirst().get()
                        .daily_priceHistory.stream()
                        .filter(p -> p.time != null && p.time > days30Ago)
                        .sorted(Comparator.comparing(p -> p.time))
                        .map(p -> p.close)
                        .collect(Collectors.toList());

                stocks.add(stock);
            }

            return stocks;
        } else {
            return Collections.emptyList();
        }
    }

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).WATCHLIST.id)")
    public List<Article> getWatchListNews() {
        String userId = PrincipalManager.getCurrentUserId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(userId));
        User user = mongoTemplate.findOne(query1, User.class);

        if (user.watchList != null) {
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("symbol").in(user.watchList));
            List<Article> articles = mongoTemplate.find(query2, Article.class);

            return articles;
        } else {
            return Collections.emptyList();
        }
    }
}
