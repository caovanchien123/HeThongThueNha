package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class Comment {
    private String id;
    private String id_room;
    private String id_person;
    private String text;
    private Timestamp time_added;

    public Comment() {
    }

    public Comment(String id, String id_room, String id_person, String text, Timestamp time_added) {
        this.id = id;
        this.id_room = id_room;
        this.id_person = id_person;
        this.text = text;
        this.time_added = time_added;
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

    public String getId_person() {
        return id_person;
    }

    public void setId_person(String id_person) {
        this.id_person = id_person;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime_added() {
        return time_added;
    }

    public void setTime_added(Timestamp time_added) {
        this.time_added = time_added;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id_room='" + id_room + '\'' +
                ", id_person='" + id_person + '\'' +
                ", text='" + text + '\'' +
                ", time_added=" + time_added +
                '}';
    }
}
