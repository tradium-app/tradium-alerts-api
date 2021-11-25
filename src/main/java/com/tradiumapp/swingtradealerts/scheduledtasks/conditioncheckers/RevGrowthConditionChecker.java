package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class RevGrowthConditionChecker extends ConditionChecker {
    private final Stock stock;
    private static final Logger logger = LoggerFactory.getLogger(RevGrowthConditionChecker.class);

    public RevGrowthConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) throws Exception {
        if (stock.revenueGrowthTTMYoy == 0) {
            String msg = "revenueGrowthTTMYoy is 0 for " + stock.symbol;
            logger.error(msg);
            throw new Exception(msg);
        }

        return compareNumbers(condition.operator, stock.revenueGrowthTTMYoy, Float.parseFloat(condition.value), condition.diff_percent);
    }
}
