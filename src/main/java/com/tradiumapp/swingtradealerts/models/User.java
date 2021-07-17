package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("users")
public class User {
    @Id
    private ObjectId id;
    public String name;
    public String authProvider;
    public String fcmToken;
    public String countryCode;
    public String timeZone;
    public String ipAddress;
    public Date createdDate;
    public Date modifiedDate;
}
