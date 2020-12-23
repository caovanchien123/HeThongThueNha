package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class BookRoom {
    private String id;
    private String id_person;
    private String id_room;
    private String type_description;
    private Timestamp bookRoomAdded;
    private boolean isConfirm;
    private double pricePaid;

    public BookRoom() {
    }

    public BookRoom(String id, String id_person, String id_room, String type_description, Timestamp bookRoomAdded, boolean isConfirm, double pricePaid) {
        this.id = id;
        this.id_person = id_person;
        this.id_room = id_room;
        this.type_description = type_description;
        this.bookRoomAdded = bookRoomAdded;
        this.isConfirm = isConfirm;
        this.pricePaid = pricePaid;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
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

    public String getId_room() {
        return id_room;
    }

    public void setId_room(String id_room) {
        this.id_room = id_room;
    }

    public String getType_description() {
        return type_description;
    }

    public void setType_description(String type_description) {
        this.type_description = type_description;
    }

    public Timestamp getBookRoomAdded() {
        return bookRoomAdded;
    }

    public void setBookRoomAdded(Timestamp bookRoomAdded) {
        this.bookRoomAdded = bookRoomAdded;
    }

    @Override
    public String toString() {
        return "BookRoom{" +
                "id='" + id + '\'' +
                ", id_person='" + id_person + '\'' +
                ", id_room='" + id_room + '\'' +
                ", type_description='" + type_description + '\'' +
                ", bookRoomAdded=" + bookRoomAdded +
                '}';
    }
}
