package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class Alert {
    @Id
    public ObjectId id;

    public String userId;
    public String symbol;
    public AlertSignal signal;
    public String title;
    public AlertStatus status;

    public List<Condition> conditions;

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();

    public enum AlertSignal {
        Buy, Sell
    }

    public enum AlertStatus {
        On, Off, Disabled
    }
}