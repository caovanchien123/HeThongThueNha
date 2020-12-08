package com.example.hethongthuenha.Person.Model;

import android.widget.ImageView;

public class PersonItemRecycleView {
    int imageItem;
    String name;

    public PersonItemRecycleView(int imageItem, String name) {
        this.imageItem = imageItem;
        this.name = name;
    }

    public int getImageItem() {
        return imageItem;
    }

    public String getName() {
        return name;
    }
}
