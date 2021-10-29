package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.IndicatorType;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class EMAConditionChecker extends ConditionChecker {
    private final Stock stock;

    public EMAConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        EMAIndicator emaIndicator = new EMAIndicator(priceIndicator, condition.config1.length);
        float emaValue = emaIndicator.getValue(emaIndicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.indicator2.equals(IndicatorType.price)) {
            float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();
            return checkAboveOrBelow(condition.operator, emaValue, lastValue, condition.diff_percent);
        } else if (condition.indicator2.equals(IndicatorType.sma)) {
            SMAIndicator smaIndicator = new SMAIndicator(priceIndicator, condition.config2.length);
            float smaValue = smaIndicator.getValue(smaIndicator.getBarSeries().getBarCount() - 1).floatValue();

            return checkAboveOrBelow(condition.operator, emaValue, smaValue, condition.diff_percent);
        } else if (condition.indicator2.equals(IndicatorType.week52High)) {
            return checkAboveOrBelow(condition.operator, emaValue, stock.week52High, condition.diff_percent);
        } else if (condition.indicator2.equals(IndicatorType.week52Low)) {
            return checkAboveOrBelow(condition.operator, emaValue, stock.week52Low, condition.diff_percent);
        } else {
            return false;
        }
    }
}
