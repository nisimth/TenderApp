package com.skyapps.bennyapp.Objects;

import android.util.Log;

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

    // private tender constructor
    public Tender(String masad, String name, String project, String startTender, String endTender, String startTime, String endTime) {
        this.masad = masad;
        this.name = name;
        this.project = project;
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

    // public tender constructor
    public Tender(String masad, String name, String email,String startTender, String endTender, String startTime, String endTime, int num) {
        this.masad = masad;
        this.name = name;
        this.time = time;
        this.email = email;
        this.num = num;
        this.startTender = startTender;
        this.endTender = endTender;
        this.startTime = startTime;
        this.endTime = endTime;
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

    // unused
    /*public Long calcTimer(String endDate, String endTime)  {
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
    }*/
    public Long calcEnds()  {
        String time = endTender + " " + endTime;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
            Log.e("diff", diff +", " + masad + ", "+ d.toString() + "(" +d.getTime() +")" + " - "+ currentDate + "(" +currentDate.getTime()+ ")"  +"");
        } catch (Exception e){

        }

        return diff;

    }

    public Long calcStarts()  {
        String time = startTender + " " + startTime;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
