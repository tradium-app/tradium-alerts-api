package com.tradiumapp.swingtradealerts.scheduledtasks.conditioncheckers

import com.tradiumapp.swingtradealerts.models.Condition
import com.tradiumapp.swingtradealerts.models.IndicatorType
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBarSeriesBuilder
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.PriceIndicator
import org.testng.annotations.Test

import java.time.Instant

class EMAConditionCheckerTest {

    @Test(groups = "unit")
    public void testCheckCondition() {
        Condition.ValueConfig valueConfig = new Condition.ValueConfig();
        valueConfig.length = 20
        valueConfig.length2 = 50
        Condition condition = new Condition(IndicatorType.ema, "golden_cross_ema20_ema50", valueConfig);

        BarSeries series = new BaseBarSeriesBuilder().build();
        for (i in 0..100) {
//            Instant instant = Instant.ofEpochSecond(stockPrice.time);
//            series.addBar(zonedDateTime, stockPrice.open, stockPrice.high, stockPrice.low, stockPrice.close, stockPrice.volume);
        }


        PriceIndicator priceIndicator = new ClosePriceIndicator(series)
        EMAConditionChecker checker = new EMAConditionChecker()

        checker.checkCondition(condition, priceIndicator)
    }
}
