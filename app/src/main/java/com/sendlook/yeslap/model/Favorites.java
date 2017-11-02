package com.sendlook.yeslap.model;

public class Favorites {

    private String uid;
    private String date;

    public Favorites() {

    }

    public Favorites(String uid, String date) {
        this.uid = uid;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
