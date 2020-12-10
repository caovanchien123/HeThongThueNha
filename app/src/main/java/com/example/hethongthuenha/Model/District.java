package com.example.hethongthuenha.Model;

import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("type")
    private String type;
    @SerializedName("tinh_id")
    private int tinh_id;

    public District(int id, String name, String location, String type, int tinh_id) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.tinh_id = tinh_id;
    }

    public District() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTinh_id() {
        return tinh_id;
    }

    public void setTinh_id(int tinh_id) {
        this.tinh_id = tinh_id;
    }
}
