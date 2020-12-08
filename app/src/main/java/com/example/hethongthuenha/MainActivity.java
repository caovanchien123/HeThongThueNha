package com.example.hethongthuenha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hethongthuenha.Person.Adapter.CustomAdapterPersonItem;
import com.example.hethongthuenha.Person.Model.PersonItemRecycleView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<PersonItemRecycleView> personItemRecycleViews;
    CustomAdapterPersonItem personItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_person);
        recyclerView = findViewById(R.id.person_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        personItemRecycleViews = new ArrayList<PersonItemRecycleView>();
        personItemRecycleViews.add(new PersonItemRecycleView(R.drawable.person_image_infomation, "Cá nhân"));
        personItemRecycleViews.add(new PersonItemRecycleView(R.drawable.person_image_payment, "Hoa hồng"));
        personItemRecycleViews.add(new PersonItemRecycleView(R.drawable.person_image_save_money, "Nạp tiền"));
        personItemRecycleViews.add(new PersonItemRecycleView(R.drawable.person_image_money_out, "Rút tiền"));
        personItemRecycleViews.add(new PersonItemRecycleView(R.drawable.person_image_logout, "Đăng xuất"));
        personItem = new CustomAdapterPersonItem(getApplicationContext(), personItemRecycleViews);
        recyclerView.setAdapter(personItem);
    }
}