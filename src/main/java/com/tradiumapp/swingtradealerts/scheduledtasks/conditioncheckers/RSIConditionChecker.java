package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.IndicatorType;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class RSIConditionChecker extends ConditionChecker {
    private final Stock stock;

    public RSIConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        RSIIndicator rsiIndicator = new RSIIndicator(priceIndicator, condition.config.length);
        float rsiValue = rsiIndicator.getValue(rsiIndicator.getBarSeries().getBarCount() - 1).floatValue();

        if (condition.indicator2.equals(IndicatorType.price)) {
            float lastValue = priceIndicator.getValue(priceIndicator.getBarSeries().getBarCount() - 1).floatValue();
            return checkAboveOrBelow(condition.operator, rsiValue, lastValue, condition.diff_percent);
        } else if (condition.indicator2.equals(IndicatorType.sma)) {
            SMAIndicator smaIndicator = new SMAIndicator(priceIndicator, condition.config.length);
            float smaValue = smaIndicator.getValue(smaIndicator.getBarSeries().getBarCount() - 1).floatValue();

            return checkAboveOrBelow(condition.operator, rsiValue, smaValue, condition.diff_percent);
        } else if (condition.indicator2.equals(IndicatorType.week52High)) {
            return checkAboveOrBelow(condition.operator, rsiValue, stock.week52High, condition.diff_percent);
        } else if (condition.indicator2.equals(IndicatorType.week52Low)) {
            return checkAboveOrBelow(condition.operator, rsiValue, stock.week52Low, condition.diff_percent);
        } else {
            return false;
        }
    }
}
