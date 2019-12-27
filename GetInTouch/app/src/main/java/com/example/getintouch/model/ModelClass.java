package com.example.getintouch.model;

public class ModelClass {

    // this model for the user info to the appbar layout

    private String userid;
    private String username;
    private String imageurl;

    public ModelClass(String userid,String username, String imageurl) {
        this.userid = userid;
        this.username = username;
        this.imageurl = imageurl;
    }

    public ModelClass() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
