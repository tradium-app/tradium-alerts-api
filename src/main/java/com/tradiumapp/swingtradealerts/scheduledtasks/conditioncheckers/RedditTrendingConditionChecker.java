package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class RedditTrendingConditionChecker implements ConditionChecker {
    private final Stock stock;

    public RedditTrendingConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        if (condition.valueConfig.upDirection && stock.redditRank < condition.valueConfig.value && stock.redditRank > 0) {
            return true;
        } else if (!condition.valueConfig.upDirection && (stock.redditRank > condition.valueConfig.value || stock.redditRank == 0)) {
            return true;
        } else {
            return false;
        }
    }
}
