package com.example.hethongthuenha.MainActivity.Fragment.MainRoom;

import com.example.hethongthuenha.Model.Room;

import java.util.List;

public interface MainRoomContract {
    interface Presenter{
        void InitRoom();
    }

    interface View{
        void InitAdapter(List<Room> rooms);

    }
}
