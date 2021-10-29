package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class RedditTrendingConditionChecker extends ConditionChecker {
    private final Stock stock;

    public RedditTrendingConditionChecker(Stock stock) {
        this.stock = stock;
    }

    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) {
        if (stock.redditRank == 0) return false;

        switch (condition.value) {
            case "top10":
                return stock.redditRank < 10;
            case "top20":
                return stock.redditRank < 20;
            case "top50":
                return stock.redditRank < 50;
        }

        return false;
    }
}
