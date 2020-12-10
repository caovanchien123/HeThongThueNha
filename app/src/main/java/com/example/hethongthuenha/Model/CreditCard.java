package com.example.hethongthuenha.Model;

public class CreditCard {
    private String id;
    private String id_person;
    private String email_person;
    private double point;
    private String number_bankcard;

    public CreditCard() {
    }


    public CreditCard(String id, String id_person, String email_person, double point, String number_bankcard) {
        this.id = id;
        this.id_person = id_person;
        this.email_person = email_person;
        this.point = point;
        this.number_bankcard = number_bankcard;
    }

    public String getId_person() {
        return id_person;
    }

    public void setId_person(String id_person) {
        this.id_person = id_person;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail_person() {
        return email_person;
    }

    public void setEmail_person(String email_person) {
        this.email_person = email_person;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getNumber_bankcard() {
        return number_bankcard;
    }

    public void setNumber_bankcard(String number_bankcard) {
        this.number_bankcard = number_bankcard;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id='" + id + '\'' +
                ", id_person='" + email_person + '\'' +
                ", point=" + point +
                ", number_bankcard='" + number_bankcard + '\'' +
                '}';
    }
}
