package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.helpers.PriceIndicator;

import static java.lang.Math.abs;

public abstract class ConditionChecker {
    public abstract boolean checkCondition(Condition condition, PriceIndicator priceIndicator) throws Exception;

    public boolean compareNumbers(Condition.Operator operator, float value1, float value2, float diff_percent) {
        if (operator == Condition.Operator.above) {
            float highPoint = (1 + abs(diff_percent) / 100) * value2;
            if (diff_percent >= 0) return value1 > highPoint;
            else return value1 > value2 && value1 < highPoint;
        } else {
            float lowPoint = (1 - abs(diff_percent) / 100) * value2;
            if (diff_percent >= 0) return value1 < lowPoint;
            else return value1 < value2 && value1 > lowPoint;
        }
    }
}
