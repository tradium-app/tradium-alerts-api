package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class SMAConditionChecker implements ConditionChecker {
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        EMAIndicator indicator = new EMAIndicator(priceIndicator, condition.valueConfig.length);
        float lastRsiValue = indicator.getValue(indicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.valueConfig.upDirection && lastRsiValue > condition.valueConfig.value) {
            return true;
        } else if (!condition.valueConfig.upDirection && lastRsiValue < condition.valueConfig.value) {
            return true;
        } else {
            return false;
        }
    }
}
