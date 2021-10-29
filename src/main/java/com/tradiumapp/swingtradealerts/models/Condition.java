package com.tradiumapp.swingtradealerts.models;

public class Condition {
    public int order;
    public String timeframe;
    public boolean isNegative;
    public IndicatorType indicator1;
    public Operator operator;
    public IndicatorType indicator2;
    public String value;
    public String valueText;
    public float diff_percent;
    public Config config1;
    public Config config2;

    public Condition() {
    }

    public Condition(IndicatorType indicator1, Operator operator, IndicatorType indicator2, String value, float diff_percent) {
        this.indicator1 = indicator1;
        this.operator = operator;
        this.value = value;
        this.indicator2 = indicator2;
        this.diff_percent = diff_percent;
    }

    public class Config {
        public int length;
    }

    public enum Operator {
        above, below
    }
}
