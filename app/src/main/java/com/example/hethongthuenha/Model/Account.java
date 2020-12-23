package com.example.hethongthuenha.Model;

public class Account {
    private String id;
    private String id_person;
    private boolean isLocked;
    private String reason;
    public Account() {
    }

    public Account(String id, String id_person, boolean isLocked, String reason) {
        this.id = id;
        this.id_person = id_person;
        this.isLocked = isLocked;
        this.reason = reason;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", id_person='" + id_person + '\'' +
                ", isLocked=" + isLocked +
                '}';
    }
}
