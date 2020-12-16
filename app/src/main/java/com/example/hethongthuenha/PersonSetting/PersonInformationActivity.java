package com.example.hethongthuenha.PersonSetting;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.PersonItemMenu;
import com.example.hethongthuenha.ModelA.Description_Room;
import com.example.hethongthuenha.ModelA.Room;
import com.example.hethongthuenha.PersonSetting.Adapter.CustomAdapterPersonInformation;
import com.example.hethongthuenha.PersonSetting.Adapter.CustomAdapterPersonMenu;
import com.example.hethongthuenha.R;

import java.util.ArrayList;

public class PersonInformationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Room> roomArrayList;
    CustomAdapterPersonInformation customAdapterPersonInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation_person_setting);

        test();
    }

    private void test() {
        recyclerView = findViewById(R.id.person_information_home);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        roomArrayList = new ArrayList<Room>();
        Room room = new Room();
        Description_Room description_room = new Description_Room();
        description_room.setAddress("aaaaaaaaaaaaa");
        description_room.setTitle("ssssssssssssss");
        description_room.setPrice(100000);
        room.setStage1(description_room);
        roomArrayList.add(room);
        roomArrayList.add(room);
        roomArrayList.add(room);
        customAdapterPersonInformation = new CustomAdapterPersonInformation(getApplicationContext(), roomArrayList);
        recyclerView.setAdapter(customAdapterPersonInformation);
    }
}
