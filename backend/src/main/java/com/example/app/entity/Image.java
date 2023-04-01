package com.example.app.entity;

public class Image {

    private String userID;
    private byte[] image;


    public Image(String id, byte[] image) {
        this.userID = id;
        this.image = image;
    }

    public String getUserID() {
        return userID;
    }

    public byte[] getImage() {
        return image;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
