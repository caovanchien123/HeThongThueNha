package com.example.hethongthuenha.Model;

import java.io.Serializable;

public class Description_Room implements Serializable{
    private String title;
    private String description;
    private String address;
    private String type_date;
    private double price;
    private double area;
    private int accommodation;
    private int amout;
    private String type_room;

    public Description_Room() {
    }

    public Description_Room(String title, String description, String address, String type_date, double price, double area, int accommodation, int amout, String type_room) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.type_date = type_date;
        this.price = price;
        this.area = area;
        this.accommodation = accommodation;
        this.amout = amout;
        this.type_room = type_room;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType_date() {
        return type_date;
    }

    public void setType_date(String type_date) {
        this.type_date = type_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(int accommodation) {
        this.accommodation = accommodation;
    }

    public int getAmout() {
        return amout;
    }

    public void setAmout(int amout) {
        this.amout = amout;
    }

    public String getType_room() {
        return type_room;
    }

    public void setType_room(String type_room) {
        this.type_room = type_room;
    }

    @Override
    public String toString() {
        return "Description_Room{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", type_date='" + type_date + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", accommodation=" + accommodation +
                ", amout=" + amout +
                ", type_room='" + type_room + '\'' +
                '}';
    }
}
