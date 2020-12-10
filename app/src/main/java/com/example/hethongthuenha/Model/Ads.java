package com.example.hethongthuenha.Model;


import com.google.firebase.Timestamp;

public class Ads {
    private String id;
    private String id_room;
    private Double price;
    private Timestamp count_down;

    public Ads() {
    }

    public Ads(String id, String id_room, Double price, Timestamp count_down) {
        this.id = id;
        this.id_room = id_room;
        this.price = price;
        this.count_down = count_down;
    }

    public Timestamp getCount_down() {
        return count_down;
    }

    public void setCount_down(Timestamp count_down) {
        this.count_down = count_down;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_room() {
        return id_room;
    }

    public void setId_room(String id_room) {
        this.id_room = id_room;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "id='" + id + '\'' +
                ", id_room='" + id_room + '\'' +
                ", price=" + price +
                '}';
    }
}
