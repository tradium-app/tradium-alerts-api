package com.tradiumapp.swingtradealerts.models;

public class Condition {
    public int order;
    public String indicator;
    public String timeframe;
    public String value;
    public ValueConfig valueConfig;

    public class ValueConfig {
        public int length;
        public String source;
        public float value;
    }
}
