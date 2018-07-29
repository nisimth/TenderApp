package com.skyapps.bennyapp.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tender {
    String name, masad, project;
    long time;
    String email, phone;
    String startTender, endTender;
    String startTime, endTime;

    int num;


    public Tender(String masad, String name, String project, String startTender, String endTender, String startTime, String endTime) {
        this.masad = masad;
        this.name = name;
        this.project = project;
        this.time = calcTimer(endTender,endTime);
        //this.time = 0;
        this.startTender = startTender;
        this.endTender = endTender;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Tender(String name, long time, String email, String phone) {
        this.name = name;
        this.time = time;
        this.email = email;
        this.phone = phone;
    }

    public Tender(String masad, String name, String email, long time, int num) {
        this.masad = masad;
        this.name = name;
        this.time = time;
        this.email = email;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMasad() {
        return masad;
    }

    public void setMasad(String masad) {
        this.masad = masad;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartTender() {
        return startTender;
    }

    public void setStartTender(String startTender) {
        this.startTender = startTender;
    }

    public String getEndTender() {
        return endTender;
    }

    public void setEndTender(String endTender) {
        this.endTender = endTender;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long calcTimer(String endDate, String endTime)  {
        String time = endDate + " " + endTime;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        Date d = null;
        Date currentDate = Calendar.getInstance().getTime();
        Long diff = null;
        try {
            d = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {

            diff = d.getTime() - currentDate.getTime();
        } catch (Exception e){

        }

        return diff;
    }


}
