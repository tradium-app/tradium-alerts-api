package com.tradiumapp.swingtradealerts.models;

public class Response {
    public Boolean success;
    public String message;
    public User user;

    public Response(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, User user){
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
