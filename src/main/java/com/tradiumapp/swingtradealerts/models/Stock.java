package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("stocks")
public class Stock {
    public ObjectId id;
    public String symbol;
    public String company;
    public Float price;
    public Float changePercent;
    public Float marketCap;
    public Float peRatio;
    public Float week52High;
    public Float week52Low;
    public Float ytdChangePercent;
    public Boolean shouldRefresh;
    public Boolean isEnabled;
    public Date createdDate;
    public Date modifiedDate;
}
