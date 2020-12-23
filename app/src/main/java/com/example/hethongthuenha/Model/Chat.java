package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class Chat {
    private String text;
    private String from_email_person;
    private String to_email_person;
    private String url;
    private Timestamp chatAdded;

    public Chat() {
    }

    public Chat(String text, String from_email_person, String to_email_person, String url, Timestamp chatAdded) {
        this.text = text;
        this.from_email_person = from_email_person;
        this.to_email_person = to_email_person;
        this.url = url;
        this.chatAdded = chatAdded;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom_email_person() {
        return from_email_person;
    }

    public void setFrom_email_person(String from_email_person) {
        this.from_email_person = from_email_person;
    }

    public String getTo_email_person() {
        return to_email_person;
    }

    public void setTo_email_person(String to_email_person) {
        this.to_email_person = to_email_person;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getChatAdded() {
        return chatAdded;
    }

    public void setChatAdded(Timestamp chatAdded) {
        this.chatAdded = chatAdded;
    }
}
