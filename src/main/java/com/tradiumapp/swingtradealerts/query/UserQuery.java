package com.tradiumapp.swingtradealerts.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserQuery implements GraphQLQueryResolver {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Stock> getWatchList() {
        Stock stock1 = new Stock();
        stock1.id = ObjectId.get();
        stock1.symbol = "tsla";
        return Arrays.asList(stock1);
    }

    public List<Stock> searchStocks(String searchTerm) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("symbol").regex(searchTerm, "gmi"),
                Criteria.where("company").regex(searchTerm, "gmi")));

        List<Stock> stocks = mongoTemplate.find(query, Stock.class);
        return stocks;
    }
}
