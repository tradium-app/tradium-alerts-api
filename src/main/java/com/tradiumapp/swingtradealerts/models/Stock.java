package com.tradiumapp.swingtradealerts.models;

import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
    public float price;
    public float changePercent;

    public boolean shouldRefresh;
    public boolean isEnabled;
    public boolean alertStatus;

    public float closeTime;
    public float latestPrice;

    public float marketCap;
    public float beta;
    public float peRatio;
    public float week52High;
    public float week52Low;
    public float ytdChangePercent;

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();
}


