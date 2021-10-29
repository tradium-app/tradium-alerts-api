package com.tradiumapp.swingtradealerts.scheduledtasks.email;

import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertEmailSender {
    @Autowired
    SendGridEmailSender sendGridEmailSender;

    public void sendEmail(User user, List<Alert> alerts) throws IOException {
        String buys = alerts.stream().filter(a -> a.signal == Alert.AlertSignal.Buy).limit(4)
                .map(a -> a.symbol).distinct().collect(Collectors.joining(","));
        String sells = alerts.stream().filter(a -> a.signal == Alert.AlertSignal.Sell).limit(4)
                .map(a -> a.symbol).distinct().collect(Collectors.joining(","));

        String subject = "";
        if (buys.length() > 0) subject += "Buy " + buys + ".. ";
        if (sells.length() > 0) subject += "Sell " + sells + "..";

        alerts.sort(Comparator.comparing((Alert a) -> a.signal));

        String message = "";
        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            message += (i + 1) + ") " + alert.signal + " " + alert.symbol + ": " + alerts.get(i).title + " <br/> ";

            for (Condition condition : alerts.get(i).conditions) {
                String operatorSymbol = !condition.isNegative
                        ? (condition.operator == Condition.Operator.above ? "  >  " : "  <  ")
                        : (condition.operator == Condition.Operator.above ? "  ≯  " : "  ≮  ");

                message += "&nbsp;&nbsp;&nbsp;" + StringUtils.capitalize(condition.timeframe) + " " + StringUtils.capitalize(condition.indicator1.toString())
                        + (condition.operator != null ? operatorSymbol : "")
                        + (condition.indicator2 != null ? StringUtils.capitalize(condition.indicator2.toString())
                        : (condition.valueText != null ? condition.valueText : condition.value))
                        + (condition.diff_percent > 0 ? " (+" + condition.diff_percent + "%)" : "")
                        + "<br/>";
            }
            message += "<br/>";
        }

        if (alerts.size() > 0) {
            try {
                sendGridEmailSender.sendEmail(user, subject, message);
            } catch (Exception ignored) {
            }
        }
    }
}
