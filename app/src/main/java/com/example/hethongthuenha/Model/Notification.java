package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class Notification {
    private String id_person_provide;
    private String id_person_created;
    private String description;
    private int type_notification;
    private Timestamp notificationAdded;


    public Notification() {
    }

    public Notification(String id_person_provide, String id_person_created, String description, int type_notification, Timestamp notificationAdded) {
        this.id_person_provide = id_person_provide;
        this.id_person_created = id_person_created;
        this.description = description;
        this.type_notification = type_notification;
        this.notificationAdded = notificationAdded;
    }

    public int getType_notification() {
        return type_notification;
    }

    public void setType_notification(int type_notification) {
        this.type_notification = type_notification;
    }

    public String getId_person_provide() {
        return id_person_provide;
    }

    public void setId_person_provide(String id_person_provide) {
        this.id_person_provide = id_person_provide;
    }

    public String getId_person_created() {
        return id_person_created;
    }

    public void setId_person_created(String id_person_created) {
        this.id_person_created = id_person_created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getNotificationAdded() {
        return notificationAdded;
    }

    public void setNotificationAdded(Timestamp notificationAdded) {
        this.notificationAdded = notificationAdded;
    }


    @Override
    public String toString() {
        return "Notification{" +
                "id_person_provide='" + id_person_provide + '\'' +
                ", id_person_created='" + id_person_created + '\'' +
                ", description='" + description + '\'' +
                ", type_notification=" + type_notification +
                ", notificationAdded=" + notificationAdded +
                '}';
    }
}
