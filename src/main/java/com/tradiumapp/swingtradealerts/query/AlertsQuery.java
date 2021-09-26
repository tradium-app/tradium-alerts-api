package com.tradiumapp.swingtradealerts.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.tradiumapp.swingtradealerts.models.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertsQuery implements GraphQLQueryResolver {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Alert> getAlerts(final String symbol) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("symbol").is(symbol));
        List<Alert> alerts = mongoTemplate.find(query1, Alert.class);

        return alerts;
    }
}
