package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class Week52HighConditionChecker extends ConditionChecker {
    private final Stock stock;

    public Week52HighConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();
        float expectedDrawDown = condition.diff_percent;
        float currentDrawDown = (stock.week52High - lastValue) * 100 / stock.week52High;

        return currentDrawDown > expectedDrawDown;
    }
}
