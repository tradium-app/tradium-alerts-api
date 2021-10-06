package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class RSIConditionChecker implements ConditionChecker {
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        RSIIndicator indicator = new RSIIndicator(priceIndicator, condition.valueConfig.length);
        float lastValue = indicator.getValue(indicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.valueConfig.upDirection && lastValue > condition.valueConfig.value) {
            return condition.operator == null || condition.operator == Condition.Operator.And;
        } else if (!condition.valueConfig.upDirection && lastValue < condition.valueConfig.value) {
            return condition.operator == null || condition.operator == Condition.Operator.And;
        } else {
            return condition.operator == Condition.Operator.Not;
        }
    }
}
