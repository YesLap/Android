package com.sendlook.yeslap;

public class FindUsers {

    private String uid;
    private String username;
    private String image;

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
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FindUsers(String uid, String username, String image) {
        this.uid = uid;
        this.username = username;
        this.image = image;
    }
}
