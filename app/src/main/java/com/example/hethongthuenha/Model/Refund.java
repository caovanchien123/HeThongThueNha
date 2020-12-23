package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class Refund {
    private String id;
    private String id_person;
    private Double price;
    private String bankCard;
    private Timestamp refundAdded;

    public Refund() {
    }

    public Refund(String id, String id_person, Double price, String bankCard,Timestamp refundAdded) {
        this.id = id;
        this.id_person = id_person;
        this.price = price;
        this.bankCard = bankCard;
        this.refundAdded = refundAdded;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public Timestamp getRefundAdded() {
        return refundAdded;
    }

    public void setRefundAdded(Timestamp refundAdded) {
        this.refundAdded = refundAdded;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "id='" + id + '\'' +
                ", id_person='" + id_person + '\'' +
                ", price=" + price +
                ", bankCard='" + bankCard + '\'' +
                ", refundAdded=" + refundAdded +
                '}';
    }
}
