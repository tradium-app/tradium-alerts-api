package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers;

import com.tradiumapp.swingtradealerts.models.Condition;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public interface ConditionChecker {
    boolean checkCondition(Condition condition, PriceIndicator priceIndicator);
}
