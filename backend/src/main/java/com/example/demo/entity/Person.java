package com.example.demo.entity;

public class Person {

    private String userID;
    private String fullName;
    private String email;

    public Person(String id, String fullName, String email) {
        this.userID = id;
        this.fullName = fullName;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "Person[id='%s', fullname='%s', email='%s']",
                userID, fullName, email);
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
