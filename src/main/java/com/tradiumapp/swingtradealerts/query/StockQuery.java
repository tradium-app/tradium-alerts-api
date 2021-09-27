package com.tradiumapp.swingtradealerts.query;

import com.tradiumapp.swingtradealerts.models.Stock;
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

    public List<Stock> searchStocks(String searchTerm) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("symbol").regex("^" + searchTerm, "gmi"),
                Criteria.where("company").regex("^" + searchTerm, "gmi")));
        query.limit(10);

        List<Stock> stocks = mongoTemplate.find(query, Stock.class);
        return stocks;
    }
}
