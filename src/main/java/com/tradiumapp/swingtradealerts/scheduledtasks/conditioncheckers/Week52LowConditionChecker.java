package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class Week52LowConditionChecker implements ConditionChecker {
    private final Stock stock;

    public Week52LowConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();
        float expectedMarkup = condition.diff_percent;
        float currentMarkup = (lastValue - stock.week52Low) * 100 / stock.week52Low;

        return currentMarkup > expectedMarkup;
    }
}
