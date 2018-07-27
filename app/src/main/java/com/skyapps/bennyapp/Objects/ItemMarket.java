package com.skyapps.bennyapp.Objects;

public class ItemMarket {
    private int number;
    private String name;
    private long mount;
    private String price;
    private String details;
    private long mqt;
    private String size;

    public ItemMarket(int number, String name, long mount, String price, String details, long mqt, String size) {
        this.number = number;
        this.name = name;
        this.mount = mount;
        this.price = price;
        this.details = details;
        this.mqt = mqt;
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMount() {
        return mount;
    }

    public void setMount(long mount) {
        this.mount = mount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getMqt() {
        return mqt;
    }

    public void setMqt(long mqt) {
        this.mqt = mqt;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
