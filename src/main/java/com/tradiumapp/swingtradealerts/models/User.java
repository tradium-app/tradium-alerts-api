package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("users")
public class User {
    private ObjectId id;

    public String name;
    public Integer age;
    public Date createdAt;
    public String nationality;
    public List<String> friendsIds;
    public List<String> articlesIds;
}
