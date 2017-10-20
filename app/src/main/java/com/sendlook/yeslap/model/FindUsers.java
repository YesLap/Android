package com.sendlook.yeslap.model;

public class FindUsers {

    private String uid;
    private String username;
    private String image1;

    public FindUsers() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getImage() {
        return image1;
    }

    public void setImage(String image) {
        this.image1 = image;
    }

    public FindUsers(String uid, String username, String image1) {
        this.uid = uid;
        this.username = username;
        this.image1 = image1;
    }
}
