package com.fortmeier.betreuerapp.model;

public class User {

    private String name;
    private String firstName;
    private String eMail;
    private String expertises;
    private String userType;
    private String lastMessage;
    private String lastMessageTime;
    private String timeStamp;
    private String timeStampMilli;

    public User(String name, String firstName, String eMail, String expertises, String userType) {
        this.name = name;
        this.firstName = firstName;
        this.eMail = eMail;
        this.expertises = expertises;
        this.userType = userType;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getExpertises() {
        return expertises;
    }

    public void setExpertises(String expertises) {
        this.expertises = expertises;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStampMilli() {
        return timeStampMilli;
    }

    public void setTimeStampMilli(String timeStampMilli) {
        this.timeStampMilli = timeStampMilli;
    }
}
