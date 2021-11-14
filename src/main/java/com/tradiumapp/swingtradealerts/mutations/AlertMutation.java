package com.tradiumapp.swingtradealerts.mutations;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tradiumapp.swingtradealerts.auth.PrincipalManager;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.AlertRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AlertMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(AlertMutation.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AlertRepository alertRepository;

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
        update.set("enabled", alert.enabled);

        Alert savedAlert = mongoTemplate.findOne(query, Alert.class);
        boolean areConditionsSame = areConditionsSame(savedAlert.conditions, alert.conditions);
        if (!areConditionsSame || !alert.enabled) {
            update.set("status", Alert.AlertStatus.Off);
        }
        update.set("modifiedDate", new Date());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Alert.class);
        boolean success = result.getModifiedCount() == 1;

        if (success) {
            logger.info("Alert for {} updated successfully.", alert.symbol);
            return new Response(true, "Alert updated successfully.");
        } else {
            return new Response(false, "Alert update failed.");
        }
    }

    private boolean areConditionsSame(List<Condition> conditions1, List<Condition> conditions2) {
        for (Condition condition1 : conditions1) {
            if (!conditions2.stream().anyMatch(c -> c.indicator1 == condition1.indicator1
                    && c.indicator2 == condition1.indicator2
                    && c.operator == condition1.operator
                    && c.diff_percent == condition1.diff_percent)) {
                return false;
            }
        }
        return true;
    }

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).ALERT.id)")
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

    @PreAuthorize("hasAuthority(T(com.tradiumapp.swingtradealerts.auth.PermissionDefinition).ALERT.id)")
    public Response copyAlertToAllStocks(final String alertId) {
        String userId = PrincipalManager.getCurrentUserId();
        User user = userRepository.findById(new ObjectId(userId)).get();

        List<Alert> alerts = alertRepository.findByUserId(userId);
        Alert originalAlert = alerts.stream().filter(a -> a.id.equals(new ObjectId(alertId))).findFirst().get();

        List<Alert> newAlerts = new ArrayList<>();

        for (String symbol : user.watchList) {
            boolean alertCopied = alerts.stream().anyMatch(a -> a.symbol.equals(symbol)
                    && a.copiedFromId != null
                    && a.copiedFromId.equals(alertId));
            if (!symbol.equals(originalAlert.symbol) && !alertCopied) {
                Alert newAlert = new Alert();
                newAlert.copiedFromId = alertId;
                newAlert.userId = userId;
                newAlert.symbol = symbol;
                newAlert.signal = originalAlert.signal;
                newAlert.status = Alert.AlertStatus.Off;
                newAlert.title = originalAlert.title + " [copy]";
                newAlert.conditions = originalAlert.conditions;

                newAlerts.add(newAlert);
            }
        }

        alertRepository.saveAll(newAlerts);

        return new Response(true, "Alert copied to all stocks");
    }
}
