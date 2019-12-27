package com.example.getintouch.model;

public class ModelClass {

    // this model for the user info to the appbar layout

    private String username;
    private String imageurl;

    public ModelClass(String username, String imageurl) {
        this.username = username;
        this.imageurl = imageurl;
    }

    public ModelClass() {
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
