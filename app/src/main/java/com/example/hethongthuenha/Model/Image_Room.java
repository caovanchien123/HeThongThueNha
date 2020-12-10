package com.example.hethongthuenha.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Image_Room implements Serializable{
    private List<String> imagesURL;

    public Image_Room() {
        imagesURL = new ArrayList<>();
    }

    public Image_Room(List<String> imagesURL) {
        this.imagesURL = imagesURL;
    }


    public List<String> getImagesURL() {
        return imagesURL;
    }

    public void setImagesURL(List<String> imagesURL) {
        this.imagesURL = imagesURL;
    }

    @Override
    public String toString() {
        return "Image_Room{" +
                "imagesURL=" + imagesURL +
                '}';
    }
}
