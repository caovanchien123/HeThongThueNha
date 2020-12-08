package com.example.hethongthuenha.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hethongthuenha.R;

public class PersonSettingActivity extends AppCompatActivity {
    private TextView tvName, tvMoney, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_person);

        setControl();
        setEvent();
    }

    private void setEvent() {

    }

    private void setControl() {
        tvEmail = findViewById(R.id.person_tv_email);
        tvMoney = findViewById(R.id.person_tv_money);
        tvName = findViewById(R.id.person_tv_name);
    }


}