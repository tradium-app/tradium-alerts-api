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
    public float marketCap;
    public float peRatio;
    public float week52High;
    public float week52Low;
    public float ytdChangePercent;
    public boolean shouldRefresh;
    public boolean isEnabled;
    public List<StockPrice> daily_priceHistory;
    public float closeTime;
    public float latestPrice;

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();

    public class StockPrice {
        @SerializedName(value = "symbol", alternate = "T")
        public String symbol;

        @SerializedName(value = "volume", alternate = "v")
        public float volume;

        @SerializedName(value = "open", alternate = "o")
        public float open;

        @SerializedName(value = "close", alternate = "c")
        public float close;

        @SerializedName(value = "high", alternate = "h")
        public float high;

        @SerializedName(value = "low", alternate = "l")
        public float low;

        @SerializedName(value = "time", alternate = "t")
        public Long time;
    }
}


