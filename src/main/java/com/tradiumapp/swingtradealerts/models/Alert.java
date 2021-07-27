package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Alert {
    @Id
    public ObjectId id;

    public String symbol;
    public String timeFrame;
    public String type;
    public String action;
    public TargetRange targetRange;
    public String title;

}
