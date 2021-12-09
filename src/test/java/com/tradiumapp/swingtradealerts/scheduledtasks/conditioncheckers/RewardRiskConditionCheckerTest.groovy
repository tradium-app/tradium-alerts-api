package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers

import com.tradiumapp.swingtradealerts.models.Condition
import com.tradiumapp.swingtradealerts.models.IndicatorType
import com.tradiumapp.swingtradealerts.models.Stock
import org.junit.Assert;
import org.testng.annotations.Test;

class RewardRiskConditionCheckerTest {

    @Test(groups = "unit")
    public void testCheckCondition() {
        Stock stock = new Stock()
        stock.price = 100f
        stock.sr = [80f, 90f, 110f, 120f]
        Condition condition = new Condition(IndicatorType.reward_risk_ratio, Condition.Operator.above, null, "1.2", 0)

        RewardRiskConditionChecker checker = new RewardRiskConditionChecker(stock)

        boolean result = checker.checkCondition(condition, null)
        Assert.assertFalse(result)
    }
}