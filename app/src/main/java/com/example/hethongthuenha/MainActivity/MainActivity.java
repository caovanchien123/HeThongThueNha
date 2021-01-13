package com.example.hethongthuenha.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.ActivitySettingPerson;
import com.example.hethongthuenha.Adminstrator.ActivityAdmintrators;
import com.example.hethongthuenha.Login.LoginActivity;
import com.example.hethongthuenha.MainActivity.Fragment.Chat.fragment_list_chat;
import com.example.hethongthuenha.MainActivity.Fragment.MainRoom.fragment_main_room;
import com.example.hethongthuenha.MainActivity.Fragment.Notification.fragment_notification;
import com.example.hethongthuenha.MainActivity.Fragment.Requiment.fragment_requiment;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Notification.Token;
import com.example.hethongthuenha.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Event();
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setFragment(R.id.mnRoom);
        SetBottomNavigation();
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void SetBottomNavigation() {
        //neu khongg phai la admin
        if (PersonAPI.getInstance().getType_person() != Person.ADMIN)
            bottomNavigationView.getMenu().removeItem(R.id.mnAdmin);
    }

    private void Event() {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.mnPerson)
                startActivity(new Intent(MainActivity.this, ActivitySettingPerson.class));
            return false;
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(v -> {
            setFragment(v.getItemId());
            return true;
        });
    }


    private void setFragment(int id) {
        switch (id) {
            case R.id.mnRoom:
                fragment = new fragment_main_room();
                break;
            case R.id.mnNeed:
                fragment = new fragment_requiment();
                break;
            case R.id.mnNotification:
                fragment = new fragment_notification();
                break;
            case R.id.mnChat:
                fragment = new fragment_list_chat();
                break;
            case R.id.mnAdmin:
                Intent intent = new Intent(MainActivity.this, ActivityAdmintrators.class);
                startActivity(intent);
                finish();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.FrameMainRoom, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else if (bottomNavigationView.getSelectedItemId() == R.id.mnRoom)
            super.onBackPressed();
        else
            bottomNavigationView.setSelectedItemId(R.id.mnRoom);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PersonAPI.getInstance() != null && PersonAPI.getInstance().isLocked()) {
            LoginActivity.locked(this);
        }
    }

    private void updateToken(String token) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Tokens");
        Token token1 = new Token(token);
        reference.document(PersonAPI.getInstance().getUid()).set(token1);
    }
}