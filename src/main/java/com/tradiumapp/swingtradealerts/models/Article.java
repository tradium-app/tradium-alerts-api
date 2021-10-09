package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Article {
    @Id
    public ObjectId id;

    public String symbol;
    public String headline;
    public String link;

    public Date createdDate = new Date();
    public Date modifiedDate = new Date();
}
