package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("articles")
public class Stock {
    private ObjectId id;
    public String symbol;
    public String company;
    public Float price;
    public Float changePercent;
    public Float marketCap;
    public Float peRatio;
    public Float week52High;
    public Float week52Low;
    public Float ytdChangePercent;
    public Date createdDate;
    public Date modifiedDate;
}
