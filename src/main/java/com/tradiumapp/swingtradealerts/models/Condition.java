package com.tradiumapp.swingtradealerts.models;

public class Condition {
    public int order;
    public Indicator indicator;
    public String timeframe;
    public String value;
    public String valueText;
    public ValueConfig valueConfig;

    public class ValueConfig {
        public int length;
        public String source;
        public float value;
        public boolean upDirection;
    }
}
