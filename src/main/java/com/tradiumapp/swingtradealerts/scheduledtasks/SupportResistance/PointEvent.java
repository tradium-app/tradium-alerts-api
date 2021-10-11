package com.tradiumapp.swingtradealerts.scheduledtasks.SupportResistance;


import java.util.Date;

public class PointEvent {
    public enum Type {
        CUT_BODY, CUT_WICK, TOUCH_DOWN_HIGHLOW, TOUCH_DOWN, TOUCH_UP_HIGHLOW, TOUCH_UP;
    }

    Type type;
    Date timestamp;
    Double scoreChange;

    public PointEvent(Type type, Date timestamp, Double scoreChange) {
        this.type = type;
        this.timestamp = timestamp;
        this.scoreChange = scoreChange;
    }

    @Override
    public String toString() {
        return "PointEvent{" +
                "type=" + type +
                ", timestamp=" + timestamp +
                ", points=" + scoreChange +
                '}';
    }
}