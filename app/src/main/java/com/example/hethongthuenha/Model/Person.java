package com.example.hethongthuenha.Model;

import android.app.Application;

public class Person {
    private String uid;
    private String fullName;
    private String email;
    private String contact;
    private String url;
    private int type_person;
    private boolean locked;

    public static int NORMAL=1;
    public static int ADMIN=2;




    public Person() {
    }

    public Person(String uid, String fullName, String email, String contact, String url, int type_person, boolean locked) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.contact = contact;
        this.url = url;
        this.type_person = type_person;
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getType_person() {
        return type_person;
    }

    public void setType_person(int type_person) {
        this.type_person = type_person;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Person{" +
                "uid='" + uid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
