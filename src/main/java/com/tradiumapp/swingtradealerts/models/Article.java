package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("articles")
public class Article {
    private ObjectId id;

    public String title;
    public Integer minutesRead;
    public String authorId;
}
