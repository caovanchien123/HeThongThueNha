package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class Requirement {
    private String id_person;
    private String id;
    private double price;
    private String  description;
    private String address;
    private String type_room;
    private Timestamp requimentAdded;
    public Requirement() {
    }

    public Requirement(String id_person, String id, double price, String description, String address, String type_room, Timestamp requimentAdded) {
        this.id_person = id_person;
        this.id = id;
        this.price = price;
        this.description=description;
        this.address = address;
        this.type_room = type_room;
        this.requimentAdded = requimentAdded;
    }

    public Timestamp getRequimentAdded() {
        return requimentAdded;
    }

    public void setRequimentAdded(Timestamp requimentAdded) {
        this.requimentAdded = requimentAdded;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getType_room() {
        return type_room;
    }

    public void setType_room(String type_room) {
        this.type_room = type_room;
    }

    @Override
    public String toString() {
        return "Requiment{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", position='" + description + '\'' +
                ", description='" + address + '\'' +
                ", type_room='" + type_room + '\'' +
                '}';
    }
}
