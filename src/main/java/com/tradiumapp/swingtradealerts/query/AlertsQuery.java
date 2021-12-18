package com.tradiumapp.swingtradealerts.query;

import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertsQuery implements GraphQLQueryResolver {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StockRepository stockRepository;

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).ALERT.id)")
    public List<Alert> getAlerts(final String symbol) {
        String userId = PrincipalManager.getCurrentUserId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("userId").is(userId));

        if (symbol != null) query1.addCriteria(Criteria.where("symbol").is(symbol));

        List<Alert> alerts = mongoTemplate.find(query1, Alert.class);

        List<Stock> stocks = stockRepository.findBySymbolIn(alerts.stream().map(a -> a.symbol).collect(Collectors.toList()));
        for (Alert alert : alerts) {
            alert.price = stocks.stream().filter(s -> s.symbol.equals(alert.symbol)).findFirst().get().price;
        }

        return alerts;
    }
}
