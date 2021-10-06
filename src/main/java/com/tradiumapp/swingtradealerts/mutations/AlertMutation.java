package com.tradiumapp.swingtradealerts.mutations;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Response;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class AlertMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(AlertMutation.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).ALERT.id)")
    public Response addAlert(final Alert alert) {
        alert.userId = PrincipalManager.getCurrentUserId();
        alert.status = Alert.AlertStatus.Off;
        mongoTemplate.save(alert);

        logger.info("Alert for {} added successfully.", alert.symbol);
        return new Response(true, "Alert save successful.", alert);
    }

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).ALERT.id)")
    public Response updateAlert(final Alert alert) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(alert.id));

        Update update = new Update();
        update.set("title", alert.title);
        update.set("signal", alert.signal);
        update.set("conditions", alert.conditions);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Alert.class);
        Boolean success = result.getModifiedCount() == 1;

        if (success) {
            logger.info("Alert for {} updated successfully.", alert.symbol);
            return new Response(true, "Alert updated successfully.");
        } else {
            return new Response(false, "Alert update failed.");
        }
    }

    public Response deleteAlert(final String alertId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(alertId));
        DeleteResult deleteResult = mongoTemplate.remove(query, Alert.class);

        if (deleteResult.getDeletedCount() == 1) {
            return new Response(true, "Alert delete successful.");
        } else {
            logger.error("Alert {} delete failed.", alertId);
            return new Response(false, "Alert delete failed.");
        }
    }
}
