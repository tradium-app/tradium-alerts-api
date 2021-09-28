package com.tradiumapp.swingtradealerts.mutations;

import com.tradiumapp.swingtradealerts.auth.service.UserService;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.models.User;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class LoginMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(LoginMutation.class);

    @Autowired
    UserService userService;

    public Response loginUser(final String accessToken) {
        try {
            User user = userService.registerUser(accessToken);
            return new Response(true, "Login successful.", user);
        } catch (Exception ex) {
            logger.error("Error during login", ex);
            return new Response(false, "Login failed.");
        }
    }
}
