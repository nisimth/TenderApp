package com.skyapps.bennyapp.tenders.notificationstabs;

public class ItemNotification {
    String username, message, type;
    String mqtNum;
    long numberTender;
    String privateorpublic;

    public ItemNotification(String username, String message, String type, String tenderNum) {
        this.username = username;
        this.message = message;
        /////
        this.mqtNum = tenderNum ;
        ////
        this.type = type;
    }
    /// chat/win notification constructor
    public ItemNotification(String username, String message, String type, String tenderNum, long numberTender) {
        this.username = username;
        this.message = message;
        this.type = type;
        this.mqtNum = tenderNum; // mqt
        this.numberTender = numberTender;
    }
    /// new Tender notification constructor
    public ItemNotification(String username, String message, String type, String mqtNum, long numberTender, String privateorpublic) {
        this.username = username;
        this.message = message;
        this.type = type;
        this.mqtNum = mqtNum;
        this.numberTender = numberTender;
        this.privateorpublic = privateorpublic;
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
    public String getMqtNum() {
        return mqtNum;
    }
    public void setMqtNum(String mqtNum) {
        this.mqtNum = mqtNum;
    }

    public long getNumberTender() {
        return numberTender;
    }

    public void setNumberTender(long numberTender) {
        this.numberTender = numberTender;
    }

    public String getPrivateorpublic() {
        return privateorpublic;
    }

    public void setPrivateorpublic(String privateorpublic) {
        this.privateorpublic = privateorpublic;
    }

    ////////
}
