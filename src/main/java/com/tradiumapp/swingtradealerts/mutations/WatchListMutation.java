package com.tradiumapp.swingtradealerts.mutations;

import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bson.types.ObjectId;
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

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).WATCHLIST.id)")
    public Response addStock(final String symbol) {
        final String symbolCap = symbol.toUpperCase(Locale.ROOT);
        logger.info("Adding stock {} to a watchlist.", symbolCap);

        String userId = PrincipalManager.getCurrentUserId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(userId));
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

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).WATCHLIST.id)")
    public Response removeStock(final String symbol) {
        final String symbolCap = symbol.toUpperCase(Locale.ROOT);
        logger.info("Removing stock {} from a watchlist.", symbolCap);

        String userId = PrincipalManager.getCurrentUserId();

        User user = userRepository.findById(new ObjectId(userId)).get();
        user.watchList.remove(symbolCap);
        user = userRepository.save(user);

        if(!user.watchList.contains(symbolCap)){
            return new Response(true, "Stock removed from the watchlist.");
        } else {
            return new Response(false, "Stock not removed from the watchlist.");
        }
    }
}
