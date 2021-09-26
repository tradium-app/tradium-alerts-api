package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("users")
public class User {
    @Id
    public ObjectId id;

    @Indexed(unique = true)
    public String firebaseUid;

    public String name;
    public String email;
    public String imageUrl;
    public List<String> watchList;
    public String authProvider;
    public String fcmToken;
    public String countryCode;
    public String timeZone;
    public String ipAddress;

    public Date createdDate;
    public Date modifiedDate;
}
