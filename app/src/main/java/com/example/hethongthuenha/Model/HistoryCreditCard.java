package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class HistoryCreditCard {
    private String id;
    private String id_person;
    private String description;
    private double point;
    private Timestamp CreditCardAdded;

    public HistoryCreditCard() {
    }

    public HistoryCreditCard(String id, String id_person, String description, double point, Timestamp creditCardAdded) {
        this.id = id;
        this.id_person = id_person;
        this.description = description;
        this.point = point;
        CreditCardAdded = creditCardAdded;
    }

    public Timestamp getCreditCardAdded() {
        return CreditCardAdded;
    }

    public void setCreditCardAdded(Timestamp creditCardAdded) {
        CreditCardAdded = creditCardAdded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_person() {
        return id_person;
    }



    public void setId_person(String id_person) {
        this.id_person = id_person;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "HistoryCreditCard{" +
                "id='" + id + '\'' +
                ", id_person='" + id_person + '\'' +
                ", description='" + description + '\'' +
                ", point=" + point +
                '}';
    }
}
