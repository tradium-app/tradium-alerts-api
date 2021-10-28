package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers

import com.tradiumapp.swingtradealerts.models.Condition
import com.tradiumapp.swingtradealerts.models.IndicatorType
import com.tradiumapp.swingtradealerts.models.Stock
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBarSeriesBuilder
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.PriceIndicator
import org.testng.annotations.Test

import java.time.Instant

class EMAConditionCheckerTest {

    @Test(groups = "unit")
    public void testCheckCondition() {
        Condition.Config config = new Condition.Config();
        config.length = 20
        Condition condition = new Condition(IndicatorType.ema, Condition.Operator.above, IndicatorType.price, null, null);

        BarSeries series = new BaseBarSeriesBuilder().build();
        for (i in 0..100) {
//            Instant instant = Instant.ofEpochSecond(stockPrice.time);
//            series.addBar(zonedDateTime, stockPrice.open, stockPrice.high, stockPrice.low, stockPrice.close, stockPrice.volume);
        }

        PriceIndicator priceIndicator = new ClosePriceIndicator(series)
        Stock stock = new Stock()
        stock.price = 22;
        EMAConditionChecker checker = new EMAConditionChecker()

        checker.checkCondition(condition, priceIndicator)
    }
}
