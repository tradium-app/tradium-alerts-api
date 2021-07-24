package com.tradiumapp.swingtradealerts.mutations;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlertMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(AlertMutation.class);

    @Autowired
    MongoTemplate mongoTemplate;

    public Response addAlert(final Alert alert) {
        mongoTemplate.save(alert);

        logger.info("{} {} alert on {} saved successfully.", alert.type, alert.action, alert.symbol);
        return new Response(true, "Alert save successful.");
    }
}
