package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public abstract class ConditionChecker {
    public abstract boolean checkCondition(Condition condition, PriceIndicator priceIndicator);

    public boolean checkAboveOrBelow(Condition.Operator operator, float value1, float value2, float diff_percent) {
        if (operator == Condition.Operator.above) {
            return value1 > (1 + diff_percent / 100) * value2;
        } else {
            return value1 < (1 - diff_percent / 100) * value2;
        }
    }
}
