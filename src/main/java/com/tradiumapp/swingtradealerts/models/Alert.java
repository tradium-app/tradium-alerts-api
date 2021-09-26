package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class Alert {
    @Id
    public ObjectId id;

    public String symbol;
    public String title;
    public AlertStatus status;

    public List<Condition> conditions;

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();
}
