package com.tradiumapp.swingtradealerts.models;

import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

public class StockHistory {
    @Id
    public ObjectId id;

    @Indexed(unique = true)
    public String symbol;

    public List<StockHistory.StockPrice> daily_priceHistory;
    public boolean shouldRefresh;
    public boolean isEnabled;

    @CreatedDate
    public Date createdDate = new Date();

    @LastModifiedDate
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

        public StockPrice(){}
    }
}
