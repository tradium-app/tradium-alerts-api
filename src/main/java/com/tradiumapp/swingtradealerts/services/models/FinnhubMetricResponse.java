package com.tradiumapp.swingtradealerts.services.models;

import com.google.gson.annotations.SerializedName;

public class FinnhubMetricResponse {
    public Metric metric;

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
}
