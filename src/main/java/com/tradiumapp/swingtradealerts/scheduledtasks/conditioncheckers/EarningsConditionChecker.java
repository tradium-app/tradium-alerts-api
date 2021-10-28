package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class EarningsConditionChecker implements ConditionChecker {
    private final Stock stock;

    public EarningsConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        int daysLeft = Days.daysBetween(new DateTime(), new DateTime(stock.nextEarningsDate)).getDays();

        return daysLeft > 0 && daysLeft < condition.valueConfig.value;
    }
}
