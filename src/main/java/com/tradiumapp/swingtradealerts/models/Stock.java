package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("stocks")
public class Stock {
    @Id
    public ObjectId id;

    @Indexed(unique = true)
    public String symbol;

    public String company;
    public String sector;
    public String industry;

    public float price;
    public float changePercent;

    public boolean shouldRefresh;
    public boolean isEnabled;

    @Transient
    public boolean alertStatus;

    @Transient
    public boolean isBuyAlert;

    @Transient
    public boolean isSellAlert;

    public float closeTime;
    public float latestPrice;

    public float marketCap;
    public float beta;
    public float peRatio;
    public float week52High;
    public float week52Low;
    public float ytdChangePercent;

    @Transient
    public List<Float> recentClosePrices;

    public float revenueGrowthQuarterlyYoy;
    public float revenueGrowthTTMYoy;
    public Date nextEarningsDate;

    public float rsi;
    public StockTrend trend;
    public float redditRank;
    public float tipranksPriceTarget;
    public List<Float> sr;

    @Transient
    public boolean isOnWatchList;

    @Transient
    public List<Alert> alerts;

    @CreatedDate
    public Date createdDate = new Date();

    @LastModifiedDate
    public Date modifiedDate = new Date();

    public enum StockTrend {
        Up, Down
    }
}


