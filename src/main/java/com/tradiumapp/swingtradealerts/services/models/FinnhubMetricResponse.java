package com.tradiumapp.swingtradealerts.services.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FinnhubMetricResponse {
    public Metric metric;
    public Series series;

    public class Metric{

        @SerializedName(value="52WeekHigh")
        public float _52WeekHigh;

        @SerializedName(value="52WeekLow")
        public float _52WeekLow;

        public float beta;
        public float marketCapitalization;

        public float revenueGrowthQuarterlyYoy;
        public float revenueGrowthTTMYoy;
    }

    public class Series {
        public Quarterly quarterly;

        public class Quarterly {
            public List<MetricValue> eps;
            public List<MetricValue> grossMargin;
            public List<MetricValue> salesPerShare;
        }

        public class MetricValue {
            public String period;
            public float v;
        }
    }
}
