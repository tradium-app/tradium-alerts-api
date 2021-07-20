
package com.tradiumapp.swingtradealerts.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WatchListQuery implements GraphQLQueryResolver {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Stock> getWatchList() {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("firebaseUid").is("6V7jHPpxTeXfUMI39HtIRGEMVv13"));
        User user = mongoTemplate.findOne(query1, User.class);

        return user.watchList;
    }
}
