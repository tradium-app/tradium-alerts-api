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
    public Float price;
    public Float changePercent;
    public Float marketCap;
    public Float peRatio;
    public Float week52High;
    public Float week52Low;
    public Float ytdChangePercent;
    public Boolean shouldRefresh;
    public Boolean isEnabled;
    public List<StockPrice> daily_priceHistory;
    public Float closeTime;
    public Float latestPrice;

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();

    public class StockPrice {
        @SerializedName(value = "symbol", alternate = "T")
        public String symbol;

        @SerializedName(value = "volume", alternate = "v")
        public Float volume;

        @SerializedName(value = "open", alternate = "o")
        public Float open;

        @SerializedName(value = "close", alternate = "c")
        public Float close;

        @SerializedName(value = "high", alternate = "h")
        public Float high;

        @SerializedName(value = "low", alternate = "l")
        public Float low;

        @SerializedName(value = "time", alternate = "t")
        public Float time;
    }
}


