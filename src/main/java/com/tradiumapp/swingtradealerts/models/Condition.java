package com.tradiumapp.swingtradealerts.models;

public class Condition {
    public int order;
    public String timeframe;
    public IndicatorType indicator1;
    public Operator operator;
    public IndicatorType indicator2;
    public float diff_percent;

    public Condition(){}

    public Condition(IndicatorType indicator1, Operator operator, IndicatorType indicator2, float diff_percent){
        this.indicator1 = indicator1;
        this.operator = operator;
        this.indicator2 = indicator2;
        this.diff_percent = diff_percent;
    }

    public class ValueConfig {
        public int length;
        public int length2;
        public String source;
        public float value;
        public boolean upDirection;
    }

    public enum Operator {
        above,below
    }
}
