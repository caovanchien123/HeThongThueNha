package com.example.hethongthuenha.Model;

import java.io.Serializable;
import java.util.List;

public class Utilities_Room implements Serializable{
    private List<String> description_utility;

    public Utilities_Room() {
    }

    public Utilities_Room(List<String> description_utility) {
        this.description_utility = description_utility;
    }

    public List<String> getDescription_utility() {
        return description_utility;
    }

    public void setDescription_utility(List<String> description_utility) {
        this.description_utility = description_utility;
    }

    @Override
    public String toString() {
        return "Utilities{" +
                "description_utility=" + description_utility +
                '}';
    }
}
