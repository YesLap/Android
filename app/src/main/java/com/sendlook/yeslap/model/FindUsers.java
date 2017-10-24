package com.sendlook.yeslap.model;

public class FindUsers {

    private String uid;
    private String username;
    private String image1;
    private String status;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FindUsers(String uid, String username, String image1, String status) {
        this.uid = uid;
        this.username = username;
        this.image1 = image1;
        this.status = status;
    }
}
