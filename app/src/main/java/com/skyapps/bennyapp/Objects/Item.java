package com.skyapps.bennyapp.Objects;

public class Item {

    private String company, name, phone, email;
    private int num;

    public Item(String company, String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.company = company;
    }

    public Item(String company, String name, String phone, String email, int num) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.num = num;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
