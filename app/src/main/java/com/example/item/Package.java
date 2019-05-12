package com.example.item;

public class Package {
    String p_id;
    String name;
    String description;
    String duration;
    String category;
    String seats;
    String qty;
    String startTime;
    String endTime;
    String price;

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getP_id() {
        return p_id;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getSeats() {
        return seats;
    }

    public String getStartTime() {
        return startTime;
    }
}
