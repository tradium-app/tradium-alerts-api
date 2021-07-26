package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Alert {
    @Id
    public ObjectId id;

    public String symbol;
    public String type;
    public String action;
    public String title;
    public Float targetValue;
}
