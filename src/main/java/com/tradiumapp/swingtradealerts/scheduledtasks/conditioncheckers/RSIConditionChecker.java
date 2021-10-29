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
        RSIIndicator rsiIndicator = new RSIIndicator(priceIndicator, condition.config1.length);
        float rsiValue = rsiIndicator.getValue(rsiIndicator.getBarSeries().getBarCount() - 1).floatValue();

        return checkAboveOrBelow(condition.operator, rsiValue, Float.parseFloat(condition.value), condition.diff_percent);
    }
}
