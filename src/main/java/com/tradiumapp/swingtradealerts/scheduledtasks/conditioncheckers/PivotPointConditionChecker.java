package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.helpers.PriceIndicator;
import org.ta4j.core.indicators.pivotpoints.PivotPointIndicator;
import org.ta4j.core.indicators.pivotpoints.TimeLevel;

import java.util.List;

public class PivotPointConditionChecker implements ConditionChecker {
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        PivotPointIndicator indicator = new PivotPointIndicator(priceIndicator.getBarSeries(), TimeLevel.MONTH);
//        RSIIndicator indicator = new RSIIndicator(priceIndicator, condition.valueConfig.length);

        int lastIndex = indicator.getBarSeries().getBarCount() - 1;
        List<Integer> bars = indicator.getBarsOfPreviousPeriod(lastIndex);

        float lastValue = indicator.getValue(lastIndex).floatValue();

        if (condition.valueConfig.upDirection && lastValue > condition.valueConfig.value) {
            return true;
        } else if (!condition.valueConfig.upDirection && lastValue < condition.valueConfig.value) {
            return true;
        } else {
            return false;
        }
    }
}
