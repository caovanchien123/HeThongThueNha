package com.example.hethongthuenha.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {

    //Document:How send object by intent
    //https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
    //https://stackoverflow.com/questions/23142893/parcelable-encountered-ioexception-writing-serializable-object-getactivity
    private String room_id;
    private String person_id;
    private int order;
    private Description_Room stage1;
    private LivingExpenses_Room stage2;
    private Image_Room stage3;
    private Utilities_Room stage4;
    private transient Timestamp timeAdded;

    public Room() {
    }

    public Room(String room_id, String person_id, int order, Description_Room stage1, LivingExpenses_Room stage2, Image_Room stage3, Utilities_Room stage4, Timestamp timeAdded) {
        this.room_id = room_id;
        this.person_id = person_id;
        this.order = order;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
        this.stage4 = stage4;
        this.timeAdded = timeAdded;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public Description_Room getStage1() {
        return stage1;
    }

    public void setStage1(Description_Room stage1) {
        this.stage1 = stage1;
    }

    public LivingExpenses_Room getStage2() {
        return stage2;
    }

    public void setStage2(LivingExpenses_Room stage2) {
        this.stage2 = stage2;
    }

    public Image_Room getStage3() {
        return stage3;
    }

    public void setStage3(Image_Room stage3) {
        this.stage3 = stage3;
    }

    public Utilities_Room getStage4() {
        return stage4;
    }

    public void setStage4(Utilities_Room stage4) {
        this.stage4 = stage4;
    }

    @Override
    public String toString() {
        return "Room{" +
                "room_id='" + room_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", order=" + order +
                ", stage1=" + stage1 +
                ", stage2=" + stage2 +
                ", stage3=" + stage3 +
                ", stage4=" + stage4 +
                ", timeAdded=" + timeAdded +
                '}';
    }
}
