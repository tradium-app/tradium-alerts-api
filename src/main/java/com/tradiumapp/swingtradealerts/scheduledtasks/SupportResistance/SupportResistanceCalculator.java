package com.tradiumapp.swingtradealerts.scheduledtasks.SupportResistance;


//import static com.perseus.analysis.constant.LevelType.RESISTANCE;
//import static com.perseus.analysis.constant.LevelType.SUPPORT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Lists;
//import com.perseus.analysis.calculator.mean.IMeanCalculator;
//import com.perseus.analysis.calculator.timeseries.ITimeSeriesCalculator;
//import com.perseus.analysis.constant.LevelType;
//import com.perseus.analysis.model.Tuple;
//import com.perseus.analysis.model.technical.Level;
//import com.perseus.analysis.model.timeseries.ITimeseries;
//import com.perseus.analysis.util.CollectionUtils;

/**
 * A support and resistance calculator.
 *
 * @author PRITESH
 *
 */
public class SupportResistanceCalculator implements
        ISupportResistanceCalculator {

    static interface LevelHelper {

        Float aggregate(List<Float> data);

        LevelType type(float level, float priceAsOfDate, final float rangePct);

        boolean withinRange(Float node, float rangePct, Float val);

    }

    static class Support implements LevelHelper {

        @Override
        public Float aggregate(final List<Float> data) {
            return Collections.min(data);
        }

        @Override
        public LevelType type(final float level, final float priceAsOfDate,
                              final float rangePct) {
            final float threshold = level * (1 - (rangePct / 100));
            return (priceAsOfDate < threshold) ? LevelType.RESISTANCE : LevelType.SUPPORT;
        }

        @Override
        public boolean withinRange(final Float node, final float rangePct,
                                   final Float val) {
            final float threshold = node * (1 + (rangePct / 100f));
            if (val < threshold)
                return true;
            return false;
        }

    }

    static class Resistance implements LevelHelper {

        @Override
        public Float aggregate(final List<Float> data) {
            return Collections.max(data);
        }

        @Override
        public LevelType type(final float level, final float priceAsOfDate,
                              final float rangePct) {
            final float threshold = level * (1 + (rangePct / 100));
            return (priceAsOfDate > threshold) ? LevelType.SUPPORT : LevelType.RESISTANCE;
        }

        @Override
        public boolean withinRange(final Float node, final float rangePct,
                                   final Float val) {
            final float threshold = node * (1 - (rangePct / 100f));
            if (val > threshold)
                return true;
            return false;
        }

    }

    private static final int SMOOTHEN_COUNT = 2;

    private static final LevelHelper SUPPORT_HELPER = new Support();

    private static final LevelHelper RESISTANCE_HELPER = new Resistance();

    private final ITimeSeriesCalculator tsCalc;

    private final IMeanCalculator meanCalc;

    public SupportResistanceCalculator(final ITimeSeriesCalculator tsCalc,
                                       final IMeanCalculator meanCalc) {
        super();
        this.tsCalc = tsCalc;
        this.meanCalc = meanCalc;
    }

    @Override
    public Tuple<List<Level>, List<Level>> identify(
            final List<Float> timeseries, final int beginIndex,
            final int endIndex, final int segmentSize, final float rangePct) {

        final List<Float> series = this.seriesToWorkWith(timeseries,
                beginIndex, endIndex);
        // Split the timeseries into chunks
        final List<List<Float>> segments = this.splitList(series, segmentSize);
        final Float priceAsOfDate = series.get(series.size() - 1);

        final List<Level> levels = Lists.newArrayList();
        this.identifyLevel(levels, segments, rangePct, priceAsOfDate,
                SUPPORT_HELPER);

        this.identifyLevel(levels, segments, rangePct, priceAsOfDate,
                RESISTANCE_HELPER);

        final List<Level> support = Lists.newArrayList();
        final List<Level> resistance = Lists.newArrayList();
        this.separateLevels(support, resistance, levels);

        // Smoothen the levels
        this.smoothen(support, resistance, rangePct);

        return new Tuple<>(support, resistance);
    }

    private void identifyLevel(final List<Level> levels,
                               final List<List<Float>> segments, final float rangePct,
                               final float priceAsOfDate, final LevelHelper helper) {

        final List<Float> aggregateVals = Lists.newArrayList();

        // Find min/max of each segment
        for (final List<Float> segment : segments) {
            aggregateVals.add(helper.aggregate(segment));
        }

        while (!aggregateVals.isEmpty()) {
            final List<Float> withinRange = new ArrayList<>();
            final Set<Integer> withinRangeIdx = new TreeSet<>();

            // Support/resistance level node
            final Float node = helper.aggregate(aggregateVals);

            // Find elements within range
            for (int i = 0; i < aggregateVals.size(); ++i) {
                final Float f = aggregateVals.get(i);
                if (helper.withinRange(node, rangePct, f)) {
                    withinRangeIdx.add(i);
                    withinRange.add(f);
                }
            }

            // Remove elements within range
            CollectionUtils.remove(aggregateVals, withinRangeIdx);

            // Take an average
            final float level = this.meanCalc.mean(
                    withinRange.toArray(new Float[] {}), 0, withinRange.size());
            final float strength = withinRange.size();

            levels.add(new Level(helper.type(level, priceAsOfDate, rangePct),
                    level, strength));

        }

    }

    private List<List<Float>> splitList(final List<Float> series,
                                        final int segmentSize) {
        final List<List<Float>> splitList = CollectionUtils
                .convertToNewLists(CollectionUtils.splitList(series,
                        segmentSize));

        if (splitList.size() > 1) {
            // If last segment it too small
            final int lastIdx = splitList.size() - 1;
            final List<Float> last = splitList.get(lastIdx);
            if (last.size() <= (segmentSize / 1.5f)) {
                // Remove last segment
                splitList.remove(lastIdx);
                // Move all elements from removed last segment to new last
                // segment
                splitList.get(lastIdx - 1).addAll(last);
            }
        }

        return splitList;
    }

    private void separateLevels(final List<Level> support,
                                final List<Level> resistance, final List<Level> levels) {
        for (final Level level : levels) {
            if (level.getType() == LevelType.SUPPORT) {
                support.add(level);
            } else {
                resistance.add(level);
            }
        }
    }

    private void smoothen(final List<Level> support,
                          final List<Level> resistance, final float rangePct) {
        for (int i = 0; i < SMOOTHEN_COUNT; ++i) {
            this.smoothen(support, rangePct);
            this.smoothen(resistance, rangePct);
        }
    }

    /**
     * Removes one of the adjacent levels which are close to each other.
     */
    private void smoothen(final List<Level> levels, final float rangePct) {
        if (levels.size() < 2)
            return;

        final List<Integer> removeIdx = Lists.newArrayList();
        Collections.sort(levels);

        for (int i = 0; i < (levels.size() - 1); i++) {
            final Level currentLevel = levels.get(i);
            final Level nextLevel = levels.get(i + 1);
            final Float current = currentLevel.getLevel();
            final Float next = nextLevel.getLevel();
            final float difference = Math.abs(next - current);
            final float threshold = (current * rangePct) / 100;

            if (difference < threshold) {
                final int remove = currentLevel.getStrength() >= nextLevel
                        .getStrength() ? i : i + 1;
                removeIdx.add(remove);
                i++; // start with next pair
            }
        }

        CollectionUtils.remove(levels, removeIdx);
    }

    private List<Float> seriesToWorkWith(final List<Float> timeseries,
                                         final int beginIndex, final int endIndex) {

        if ((beginIndex == 0) && (endIndex == timeseries.size()))
            return timeseries;

        return timeseries.subList(beginIndex, endIndex);

    }

}