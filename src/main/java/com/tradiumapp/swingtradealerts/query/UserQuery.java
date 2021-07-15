package com.tradiumapp.swingtradealerts.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserQuery implements GraphQLQueryResolver {
    @Autowired
    private UserService userService;

    public List<Stock> getWatchList(String id) {
//        return userService.findAllUsers();
        Stock stock1 = new Stock();
        stock1.id = ObjectId.get();
        stock1.symbol = "tsla";
        return Arrays.asList(stock1);
    }
}
