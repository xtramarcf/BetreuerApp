package com.fortmeier.betreuerapp.model;


public class ChatMessage {

    private String userEmail;
    private String message;
    private String dateTime;
    private String timestamp;

    //for firebase getting data back
    public ChatMessage() {
    }

    public ChatMessage(String userEmail, String message, String dateTime, String timestamp) {
        this.userEmail = userEmail;
        this.message = message;
        this.dateTime = dateTime;
        this.timestamp = timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
