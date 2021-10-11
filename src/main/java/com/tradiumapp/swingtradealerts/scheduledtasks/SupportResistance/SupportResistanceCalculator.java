package com.tradiumapp.swingtradealerts.scheduledtasks.SupportResistance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class SupportResistanceCalculator {
    private static final Logger logger = LoggerFactory.getLogger(SupportResistanceCalculator.class);

    private void findSupportResistance(List<Candle> candles, Long scripId) throws ExecutionException {
        // This is a cron job, so I skip for some time once a SR is found in a stock
        if (processedCandles.getIfPresent(scripId) == null || checkAlways) {
            //Combining small candles to get larger candles of required timeframe. ( I have 1 minute candles and here creating 1 Hr candles)
            List<Candle> cumulativeCandles = cumulativeCandleHelper.getCumulativeCandles(candles, CUMULATIVE_CANDLE_SIZE);
            //Tell whether each point is a high(higher than two candles on each side) or a low(lower than two candles on each side)
            List<Boolean> highLowValueList = this.highLow.findHighLow(cumulativeCandles);
            String name = scripIdCache.getScripName(scripId);
            Set<Double> impPoints = new HashSet<Double>();
            int pos = 0;
            for (Candle candle : cumulativeCandles) {
                //A candle is imp only if it is the highest / lowest among #CONSECUTIVE_CANDLE_TO_CHECK_MIN on each side
                List<Candle> subList = cumulativeCandles.subList(Math.max(0, pos - CONSECUTIVE_CANDLE_TO_CHECK_MIN),
                        Math.min(cumulativeCandles.size(), pos + CONSECUTIVE_CANDLE_TO_CHECK_MIN));
                if (subList.stream().min(Comparator.comparing(Candle::getLow)).get().getLow().equals(candle.getLow()) ||
                        subList.stream().min(Comparator.comparing(Candle::getHigh)).get().getHigh().equals(candle.getHigh())) {
                    impPoints.add(candle.getHigh());
                    impPoints.add(candle.getLow());
                }
                pos++;
            }
            Iterator<Double> iterator = impPoints.iterator();
            List<PointScore> score = new ArrayList<PointScore>();
            while (iterator.hasNext()) {
                Double currentValue = iterator.next();
                //Get score of each point
                score.add(getScore(cumulativeCandles, highLowValueList, currentValue));
            }
            score.sort((o1, o2) -> o2.getScore().compareTo(o1.getScore()));
            List<Double> used = new ArrayList<Double>();
            int total = 0;
            Double min = getMin(cumulativeCandles);
            Double max = getMax(cumulativeCandles);
            for (PointScore pointScore : score) {
                // Each point should have at least #MIN_SCORE_TO_PRINT point
                if (pointScore.getScore() < MIN_SCORE_TO_PRINT) {
                    break;
                }
                //The extremes always come as a Strong SR, so I remove some of them
                // I also reject a price which is very close the one already used
                if (!similar(pointScore.getPoint(), used) && !closeFromExtreme(pointScore.getPoint(), min, max)) {
                    logger.info("Strong SR for scrip {} at {} and score {}", name, pointScore.getPoint(), pointScore.getScore());
//                    logger.info("Events at point are {}", pointScore.getPointEventList());
                    used.add(pointScore.getPoint());
                    total += 1;
                }
                if (total >= totalPointsToPrint) {
                    break;
                }
            }
        }
    }

    private boolean closeFromExtreme(Double key, Double min, Double max) {
        return Math.abs(key - min) < (min * DIFF_PERC_FROM_EXTREME / 100.0) || Math.abs(key - max) < (max * DIFF_PERC_FROM_EXTREME / 100);
    }

    private Double getMin(List<Candle> cumulativeCandles) {
        return cumulativeCandles.stream()
                .min(Comparator.comparing(Candle::getLow)).get().getLow();
    }

    private Double getMax(List<Candle> cumulativeCandles) {
        return cumulativeCandles.stream()
                .max(Comparator.comparing(Candle::getLow)).get().getHigh();
    }

    private boolean similar(Double key, List<Double> used) {
        for (Double value : used) {
            if (Math.abs(key - value) <= (DIFF_PERC_FOR_INTRASR_DISTANCE * value / 100)) {
                return true;
            }
        }
        return false;
    }

    private PointScore getScore(List<Candle> cumulativeCandles, List<Boolean> highLowValueList, Double price) {
        List<PointEvent> events = new ArrayList<>();
        Double score = 0.0;
        int pos = 0;
        int lastCutPos = -10;
        for (Candle candle : cumulativeCandles) {
            //If the body of the candle cuts through the price, then deduct some score
            if (cutBody(price, candle) && (pos - lastCutPos > MIN_DIFF_FOR_CONSECUTIVE_CUT)) {
                score += scoreForCutBody;
                lastCutPos = pos;
                events.add(new PointEvent(PointEvent.Type.CUT_BODY, candle.getTimestamp(), scoreForCutBody));
                //If the wick of the candle cuts through the price, then deduct some score
            } else if (cutWick(price, candle) && (pos - lastCutPos > MIN_DIFF_FOR_CONSECUTIVE_CUT)) {
                score += scoreForCutWick;
                lastCutPos = pos;
                events.add(new PointEvent(PointEvent.Type.CUT_WICK, candle.getTimestamp(), scoreForCutWick));
                //If the if is close the high of some candle and it was in an uptrend, then add some score to this
            } else if (touchHigh(price, candle) && inUpTrend(cumulativeCandles, price, pos)) {
                Boolean highLowValue = highLowValueList.get(pos);
                //If it is a high, then add some score S1
                if (highLowValue != null && highLowValue) {
                    score += scoreForTouchHighLow;
                    events.add(new PointEvent(PointEvent.Type.TOUCH_UP_HIGHLOW, candle.getTimestamp(), scoreForTouchHighLow));
                    //Else add S2. S2 > S1
                } else {
                    score += scoreForTouchNormal;
                    events.add(new PointEvent(PointEvent.Type.TOUCH_UP, candle.getTimestamp(), scoreForTouchNormal));
                }
                //If the if is close the low of some candle and it was in an downtrend, then add some score to this
            } else if (touchLow(price, candle) && inDownTrend(cumulativeCandles, price, pos)) {
                Boolean highLowValue = highLowValueList.get(pos);
                //If it is a high, then add some score S1
                if (highLowValue != null && !highLowValue) {
                    score += scoreForTouchHighLow;
                    events.add(new PointEvent(PointEvent.Type.TOUCH_DOWN, candle.getTimestamp(), scoreForTouchHighLow));
                    //Else add S2. S2 > S1
                } else {
                    score += scoreForTouchNormal;
                    events.add(new PointEvent(PointEvent.Type.TOUCH_DOWN_HIGHLOW, candle.getTimestamp(), scoreForTouchNormal));
                }
            }
            pos += 1;
        }
        return new PointScore(price, score, events);
    }

    private boolean inDownTrend(List<Candle> cumulativeCandles, Double price, int startPos) {
        //Either move #MIN_PERC_FOR_TREND in direction of trend, or cut through the price
        for (int pos = startPos; pos >= 0; pos--) {
            Candle candle = cumulativeCandles.get(pos);
            if (candle.getLow() < price) {
                return false;
            }
            if (candle.getLow() - price > (price * MIN_PERC_FOR_TREND / 100)) {
                return true;
            }
        }
        return false;
    }

    private boolean inUpTrend(List<Candle> cumulativeCandles, Double price, int startPos) {
        for (int pos = startPos; pos >= 0; pos--) {
            Candle candle = cumulativeCandles.get(pos);
            if (candle.getHigh() > price) {
                return false;
            }
            if (price - candle.getLow() > (price * MIN_PERC_FOR_TREND / 100)) {
                return true;
            }
        }
        return false;
    }

    private boolean touchHigh(Double price, Candle candle) {
        Double high = candle.getHigh();
        Double ltp = candle.getLtp();
        return high <= price && Math.abs(high - price) < ltp * DIFF_PERC_FOR_CANDLE_CLOSE / 100;
    }

    private boolean touchLow(Double price, Candle candle) {
        Double low = candle.getLow();
        Double ltp = candle.getLtp();
        return low >= price && Math.abs(low - price) < ltp * DIFF_PERC_FOR_CANDLE_CLOSE / 100;
    }

    private boolean cutBody(Double point, Candle candle) {
        return Math.max(candle.getOpen(), candle.getClose()) > point && Math.min(candle.getOpen(), candle.getClose()) < point;
    }

    private boolean cutWick(Double price, Candle candle) {
        return !cutBody(price, candle) && candle.getHigh() > price && candle.getLow() < price;
    }
}
