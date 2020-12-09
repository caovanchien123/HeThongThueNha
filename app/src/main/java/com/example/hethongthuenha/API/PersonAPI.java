package com.example.hethongthuenha.API;

import android.app.Application;

public class PersonAPI extends Application {
    private String uid;
    private String name;
    private String email;
    private int type_person;
    private boolean isLocked;
    private double point;
    private static PersonAPI instance;

    public static PersonAPI getInstance() {
        if (instance == null)
            instance = new PersonAPI();
        return instance;
    }

    public int getType_person() {
        return type_person;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void setType_person(int type_person) {
        this.type_person = type_person;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "PersonAPI{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
