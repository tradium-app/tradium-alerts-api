package com.tradiumapp.swingtradealerts.models;

public class Response {
    public Boolean success;
    public String message;

    public Response(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
