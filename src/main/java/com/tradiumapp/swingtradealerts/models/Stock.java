package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("stocks")
public class Stock {
    @Id
    public ObjectId id;

    @Indexed(unique = true)
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

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();
}
