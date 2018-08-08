package com.skyapps.bennyapp.tenders.notificationstabs;

public class ItemNotification {
    String username, message, type;
    String tenderNum;
    long numberTender;

    public ItemNotification(String username, String message, String type, String tenderNum) {
        this.username = username;
        this.message = message;
        /////
        this.tenderNum = tenderNum ;
        ////
        this.type = type;
    }

    public ItemNotification(String username, String message, String type, String tenderNum, long numberTender) {
        this.username = username;
        this.message = message;
        this.type = type;
        this.tenderNum = tenderNum; // mqt
        this.numberTender = numberTender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    ////////
    public String getTenderNum() {
        return tenderNum;
    }
    public void setTenderNum(String tenderNum) {
        this.tenderNum = tenderNum;
    }

    public long getNumberTender() {
        return numberTender;
    }

    public void setNumberTender(long numberTender) {
        this.numberTender = numberTender;
    }

    ////////
}
