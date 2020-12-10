package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Commission {
    private String id_person;
    private double price;
    private int totalDay;
    private Timestamp lastPaid;

    public Commission(String id_person, double price, int totalDay, Timestamp lastPaid) {
        this.id_person = id_person;
        this.price = price;
        this.totalDay = totalDay;
        this.lastPaid = lastPaid;
    }

    public Timestamp getLastPaid() {
        return lastPaid;
    }

    public void setLastPaid(Timestamp lastPaid) {
        this.lastPaid = lastPaid;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public Commission() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId_person() {
        return id_person;
    }

    public void setId_person(String id_person) {
        this.id_person = id_person;
    }


    @Override
    public String toString() {
        return "Commission{" +
                "id_person='" + id_person + '\'' +
                ", price=" + price +
                '}';
    }
}
