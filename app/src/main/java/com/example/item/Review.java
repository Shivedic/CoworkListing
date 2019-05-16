package com.example.item;

public class Review {
    String userName;
    String tag;
    String date;
    String desc;
    String response;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getResponse() {
        return response;
    }

    public String getTag() {
        return tag;
    }
}
