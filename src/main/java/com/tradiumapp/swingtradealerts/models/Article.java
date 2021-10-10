package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document
public class Article {
    @Id
    public ObjectId id;

    public String symbol;

    @Indexed(unique = true)
    public String headline;

    @Indexed(unique = true)
    public String link;

    @CreatedDate
    public Date createdDate;

    @LastModifiedDate
    public Date modifiedDate;

    @Field
    @Indexed(name = "expiresAtIndex", expireAfterSeconds = 2_592_000)
    Date expiresAt;
}
