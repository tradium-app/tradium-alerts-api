package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class EarningsConditionChecker extends ConditionChecker {
    private final Stock stock;

    public EarningsConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        int daysLeft = Days.daysBetween(new DateTime(), new DateTime(stock.nextEarningsDate)).getDays();

        if (daysLeft < 0) return false;

        switch (condition.value) {
            case "in_10_days":
                return daysLeft < 10;
            case "in_20_days":
                return daysLeft < 20;
            case "in_30_days":
                return daysLeft < 30;
        }

        return false;
    }
}
