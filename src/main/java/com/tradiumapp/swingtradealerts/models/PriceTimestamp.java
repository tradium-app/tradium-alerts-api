package com.tradiumapp.swingtradealerts.models;

import java.util.Date;

public class PriceTimestamp {
    public Float timestamp;
    public Float price;

    public PriceTimestamp(){}

    public PriceTimestamp(Float timestamp, Float price){
        this.timestamp = timestamp;
        this.price = price;
    }
}
