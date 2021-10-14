package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

import java.util.Arrays;

public class EMAConditionChecker implements ConditionChecker {
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();

        EMAIndicator indicator = new EMAIndicator(priceIndicator, condition.valueConfig.length);
        float emaValue = indicator.getValue(indicator.getBarSeries().getBarCount() - 1).floatValue();

        if (Arrays.asList("golden_cross_ema20_ema50", "death_cross_ema20_ema50").contains(condition.value)) {
            EMAIndicator indicator2 = new EMAIndicator(priceIndicator, condition.valueConfig.length2);
            float emaValue2 = indicator2.getValue(indicator2.getBarSeries().getBarCount() - 1).floatValue();

            return condition.valueConfig.upDirection ? emaValue > emaValue2 : emaValue < emaValue2;
        }

        if (condition.valueConfig.upDirection && lastValue > (1 + condition.valueConfig.value) * emaValue) {
            return true;
        } else if (!condition.valueConfig.upDirection && lastValue < (1 - condition.valueConfig.value) * emaValue) {
            return true;
        } else {
            return false;
        }
    }
}
