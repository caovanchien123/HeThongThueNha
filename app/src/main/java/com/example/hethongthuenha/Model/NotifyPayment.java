package com.example.hethongthuenha.Model;

public class NotifyPayment {
    private String idUser;
    private double price;
    private String type;
    private boolean isProress;

    public NotifyPayment(String idUser, double price, String type, boolean isProress) {
        this.idUser = idUser;
        this.price = price;
        this.type = type;
        this.isProress = isProress;
    }

    public boolean isProress() {
        return isProress;
    }

    public void setProress(boolean proress) {
        isProress = proress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NotifyPayment() {
    }

    public String getIdUser() {
        return idUser;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
