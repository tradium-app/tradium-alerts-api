package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

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
    public Date alertOnDate;
    public boolean enabled = true;

    public List<Condition> conditions;

    @CreatedDate
    public Date createdDate;

    @LastModifiedDate
    public Date modifiedDate;

    public enum AlertSignal {
        Buy, Sell
    }

    public enum AlertStatus {
        On, Off
    }
}