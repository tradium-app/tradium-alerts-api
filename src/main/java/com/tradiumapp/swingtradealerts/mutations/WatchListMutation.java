package com.tradiumapp.swingtradealerts.mutations;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Locale;

@Component
public class WatchListMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(WatchListMutation.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).WATCHLIST_ADMIN.id)")
    public Response addStock(final String symbol) {
        final String symbolCap = symbol.toUpperCase(Locale.ROOT);
        logger.info("Adding stock {} to a watchlist.", symbolCap);

        String firebaseUid = PrincipalManager.getCurrentUserId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("firebaseUid").is(firebaseUid));
        User user = mongoTemplate.findOne(query1, User.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("symbol").is(symbolCap));
        Stock stock = mongoTemplate.findOne(query2, Stock.class);

        if (user.watchList == null) user.watchList = new ArrayList<>();
        if (stock != null && !user.watchList.contains(symbolCap)) {
            user.watchList.add(symbolCap);
            mongoTemplate.save(user);

            return new Response(true, "Stock added to the watchlist.");
        }

        return new Response(false, "Stock not added to the watchlist.");
    }
}
