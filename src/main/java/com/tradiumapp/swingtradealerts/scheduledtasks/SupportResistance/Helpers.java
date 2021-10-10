package com.tradiumapp.swingtradealerts.scheduledtasks.SupportResistance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

enum LevelType {
    SUPPORT, RESISTANCE
}

class Tuple<A, B> {

    private final A a;

    private final B b;

    public Tuple(final A a, final B b) {
        super();
        this.a = a;
        this.b = b;
    }

    public final A getA() {
        return this.a;
    }

    public final B getB() {
        return this.b;
    }

    @Override
    public String toString() {
        return "Tuple [a=" + this.a + ", b=" + this.b + "]";
    }
}

abstract class CollectionUtils {

    /**
     * Removes items from the list based on their indexes.
     *
     * @param list    list
     * @param indexes indexes this collection must be sorted in ascending order
     */
    public static <T> void remove(final List<T> list,
                                  final Collection<Integer> indexes) {
        int i = 0;
        for (final int idx : indexes) {
            list.remove(idx - i++);
        }
    }

    /**
     * Splits the given list in segments of the specified size.
     *
     * @param list        list
     * @param segmentSize segment size
     * @return segments
     */
    public static <T> List<List<T>> splitList(final List<T> list,
                                              final int segmentSize) {
        int from = 0, to = 0;
        final List<List<T>> result = new ArrayList<>();

        while (from < list.size()) {
            to = from + segmentSize;
            if (to > list.size()) {
                to = list.size();
            }
            result.add(list.subList(from, to));
            from = to;
        }

        return result;
    }

    public static List<List<Float>> convertToNewLists(List<List<Float>> splitList) {
    }
}

/**
 * This class represents a support / resistance level.
 *
 * @author PRITESH
 */
class Level implements Serializable {

    private static final long serialVersionUID = -7561265699198045328L;

    private final LevelType type;

    private final float level, strength;

    public Level(final LevelType type, final float level) {
        this(type, level, 0f);
    }

    public Level(final LevelType type, final float level, final float strength) {
        super();
        this.type = type;
        this.level = level;
        this.strength = strength;
    }

    public final LevelType getType() {
        return this.type;
    }

    public final float getLevel() {
        return this.level;
    }

    public final float getStrength() {
        return this.strength;
    }

    @Override
    public String toString() {
        return "Level [type=" + this.type + ", level=" + this.level
                + ", strength=" + this.strength + "]";
    }

}