package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class EMAConditionChecker implements ConditionChecker {
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();

        EMAIndicator indicator = new EMAIndicator(priceIndicator, condition.valueConfig.length);
        float emaValue = indicator.getValue(indicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.valueConfig.upDirection && lastValue > (1 + condition.valueConfig.value) * emaValue) {
            return true;
        } else if (!condition.valueConfig.upDirection && lastValue < (1 - condition.valueConfig.value) * emaValue) {
            return true;
        } else {
            return false;
        }
    }
}
