package com.tradiumapp.swingtradealerts.mutations;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
//import com.tradiumapp.swingtradealerts.controllers.MainController;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WatchlistMutation implements GraphQLMutationResolver {
    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(WatchlistMutation.class);

    public Response addStock(final String stock) {
        logger.info("inside addStock");
        return new Response(true, "test message");
    }
}
