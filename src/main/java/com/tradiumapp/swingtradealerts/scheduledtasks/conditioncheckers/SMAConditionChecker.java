package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class SMAConditionChecker implements ConditionChecker {
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();

        SMAIndicator indicator = new SMAIndicator(priceIndicator, condition.valueConfig.length);
        float smaValue = indicator.getValue(indicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.valueConfig.upDirection && lastValue > (1 + condition.valueConfig.value) * smaValue) {
            return condition.operator == null || condition.operator == Condition.Operator.And;
        } else if (!condition.valueConfig.upDirection && lastValue < (1 - condition.valueConfig.value) * smaValue) {
            return condition.operator == null || condition.operator == Condition.Operator.And;
        } else {
            return condition.operator == Condition.Operator.Not;
        }
    }
}
