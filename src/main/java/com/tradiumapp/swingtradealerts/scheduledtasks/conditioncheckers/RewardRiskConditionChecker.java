package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import com.tradiumapp.swingtradealerts.models.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.indicators.helpers.PriceIndicator;

import java.util.Comparator;

public class RewardRiskConditionChecker extends ConditionChecker {
    private final Stock stock;
    private static final Logger logger = LoggerFactory.getLogger(RevGrowthConditionChecker.class);

    public RewardRiskConditionChecker(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean checkCondition(Condition condition, PriceIndicator priceIndicator) throws Exception {
        float nearestResistance = stock.sr.stream().filter(s -> s > stock.price).min(Comparator.comparing(s -> s)).get();
        float nearestSupport = stock.sr.stream().filter(s -> s < stock.price).max(Comparator.comparing(s -> s)).get();

        float reward_risk_ratio = (nearestResistance - stock.price) / (stock.price - nearestSupport);

        return compareNumbers(condition.operator, reward_risk_ratio, Float.parseFloat(condition.value), condition.diff_percent);
    }
}
