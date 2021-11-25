package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class PriceSalesConditionChecker extends ConditionChecker {
    private final Stock stock;
    private static final Logger logger = LoggerFactory.getLogger(RevGrowthConditionChecker.class);

    public PriceSalesConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) throws Exception {
        float price_by_sales_ttm = 0;
        if (stock.salesPerShareTTM != 0) price_by_sales_ttm = stock.price / stock.salesPerShareTTM;

        if (price_by_sales_ttm == 0) {
            String msg = "Price/Sales TTM is 0 for " + stock.symbol;
            logger.error(msg);
            throw new Exception(msg);
        }

        return compareNumbers(condition.operator, price_by_sales_ttm, Float.parseFloat(condition.value), condition.diff_percent);
    }
}
