package com.example.hethongthuenha.Model;

import com.google.firebase.Timestamp;

public class HistoryChat {
    private String pathChat;
    private Timestamp ChatAdded;
    private String lastChat;
    private String fromATo;

    public HistoryChat() {
    }


    public HistoryChat(String pathChat, Timestamp chatAdded, String lastChat, String fromATo) {
        this.pathChat = pathChat;
        ChatAdded = chatAdded;
        this.lastChat = lastChat;
        this.fromATo = fromATo;
    }

    public String getFromATo() {
        return fromATo;
    }

    public void setFromATo(String fromATo) {
        this.fromATo = fromATo;
    }

    public String getPathChat() {
        return pathChat;
    }

    public void setPathChat(String pathChat) {
        this.pathChat = pathChat;
    }

    public Timestamp getChatAdded() {
        return ChatAdded;
    }

    public void setChatAdded(Timestamp chatAdded) {
        ChatAdded = chatAdded;
    }

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    @Override
    public String toString() {
        return "HistoryChat{" +
                "pathChat='" + pathChat + '\'' +
                ", ChatAdded=" + ChatAdded +
                ", lastChat='" + lastChat + '\'' +
                '}';
    }
}
