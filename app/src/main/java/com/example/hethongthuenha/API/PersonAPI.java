package com.example.hethongthuenha.API;

import android.app.Application;

import java.util.ArrayList;

public class PersonAPI extends Application {
    private String uid;
    private String name;
    private String email;
    private String password;
    private int type_person;
    private boolean isLocked;
    private double point;
    private static PersonAPI instance;

    public static PersonAPI getInstance() {
        if (instance == null)
            instance = new PersonAPI();
        return instance;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getMoney(){
        double a = point;
        String ketQua = "";
        ArrayList<Double> list = new ArrayList();

        while (a != 0) {
            double swap = a % 1000;
            System.out.println(swap);
            list.add(swap);
            a = (double) ((int) a / 1000);
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            int value = (int) Math.round(list.get(i));
            if (i == list.size() - 1) {
                ketQua += value;
                if(list.size() - 1 != 0){
                    ketQua += ".";
                }
            } else if(i != 0) {
                if (list.get(i) < 10) {
                    ketQua += "00" + value+ ".";
                } else if (list.get(i) < 100) {
                    ketQua += "0" + value+ ".";
                } else {
                    ketQua += "" + value+ ".";
                }
            }else{
                if (list.get(i) < 10) {
                    ketQua += "00" + value;
                } else if (list.get(i) < 100) {
                    ketQua += "0" + value;
                } else {
                    ketQua += "" + value;
                }
            }
        }

        return ketQua + " VNĐ";
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
