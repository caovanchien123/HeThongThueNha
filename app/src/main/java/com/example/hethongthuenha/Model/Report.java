package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class Report {
    private String id;
    private String id_person;
    private String id_room;
    private String type_report;
    private String description;
    private Timestamp reportAdded;
    public Report() {
    }

    public Report(String id, String id_person, String id_room, String type_report, String description, Timestamp reportAdded) {
        this.id = id;
        this.id_person = id_person;
        this.id_room = id_room;
        this.type_report = type_report;
        this.description = description;
        this.reportAdded = reportAdded;
    }

    public Timestamp getReportAdded() {
        return reportAdded;
    }

    public void setReportAdded(Timestamp reportAdded) {
        this.reportAdded = reportAdded;
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

    public String getType_report() {
        return type_report;
    }

    public void setType_report(String type_report) {
        this.type_report = type_report;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
