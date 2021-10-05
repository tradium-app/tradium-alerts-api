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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockQuery implements GraphQLQueryResolver {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    StockRepository stockRepository;

    public List<Stock> searchStocks(String searchTerm) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("symbol").regex("^" + searchTerm, "gmi"),
                Criteria.where("company").regex("^" + searchTerm, "gmi")));
        query.limit(10);

        List<Stock> stocks = mongoTemplate.find(query, Stock.class);
        return stocks;
    }

    public Stock getStockProfile(String symbol) {
        symbol = symbol.toUpperCase();
        Stock stock = stockRepository.findBySymbol(symbol);

        String userId = PrincipalManager.getCurrentUserId();

        if (stock != null && userId != null) {
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("userId").is(userId));

            if (symbol != null) query1.addCriteria(Criteria.where("symbol").is(symbol));

            List<Alert> alerts = mongoTemplate.find(query1, Alert.class);
            stock.alerts = alerts;
        }

        return stock;
    }
}
