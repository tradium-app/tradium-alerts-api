package com.tradiumapp.swingtradealerts.mutations;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WatchlistMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(WatchlistMutation.class);

    @Autowired
    MongoTemplate mongoTemplate;

    public Response addStock(final String symbol) {
        logger.info("Adding stock {} to a watchlist.", symbol);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("firebaseUid").is("6V7jHPpxTeXfUMI39HtIRGEMVv13"));
        User user = mongoTemplate.findOne(query1, User.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("symbol").is(symbol));
        Stock stock = mongoTemplate.findOne(query2, Stock.class);

        user.watchList = new ArrayList<>();
        user.watchList.add(stock);

        mongoTemplate.save(user);

        return new Response(true, "test message");
    }
}
