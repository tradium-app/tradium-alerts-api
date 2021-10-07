package com.tradiumapp.swingtradealerts.models;

public class Condition {
    public int order;
    public Operator operator;
    public IndicatorType indicator;
    public String timeframe;
    public String value;
    public String valueText;
    public ValueConfig valueConfig;

    public Condition(){}

    public Condition(IndicatorType indicator, String value, ValueConfig valueConfig){
        this.indicator = indicator;
        this.value = value;
        this.valueConfig = valueConfig;
    }

    public class ValueConfig {
        public int length;
        public String source;
        public float value;
        public boolean upDirection;
    }

    public enum Operator {
        And, Not
    }
}
