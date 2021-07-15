package com.tradiumapp.swingtradealerts.dataFetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.services.UserService;

import java.util.List;

@Component
public class AllUsersDataFetcher implements DataFetcher<List<User>> {

    private final UserService userService;

    @Autowired
    AllUsersDataFetcher(UserService userService){
        this.userService = userService;
    }

    @Override
    public List<User> get(DataFetchingEnvironment env) {
//        User user =  env.getSource();
        List<User> users = userService.findAllUsers();

        return users;
    }
}
