package com.example.hethongthuenha.Model;

public class PhongTro {
    String roomName,roomPrice;

    public PhongTro() {
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public PhongTro(String roomName, String roomPrice) {
        this.roomName = roomName;
        this.roomPrice = roomPrice;
    }


}
