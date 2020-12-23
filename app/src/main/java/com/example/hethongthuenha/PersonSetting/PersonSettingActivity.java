package com.example.hethongthuenha.PersonSetting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Model.PersonItemMenu;
import com.example.hethongthuenha.ModelA.Model.Person;
import com.example.hethongthuenha.PersonSetting.Adapter.CustomAdapterPersonMenu;
import com.example.hethongthuenha.PersonSetting.Dialog.PersonDialogPayment;
import com.example.hethongthuenha.PersonSetting.Dialog.PersonDialogPurchase;
import com.example.hethongthuenha.PersonSetting.Dialog.PersonDialogRefund;
import com.example.hethongthuenha.R;

import java.util.ArrayList;

public class PersonSettingActivity extends AppCompatActivity {
    private TextView tvName, tvMoney, tvEmail;
    RecyclerView recyclerView;
    ArrayList<PersonItemMenu> personItemRecycleViews;
    CustomAdapterPersonMenu personItem;
    String sName, sEmail;
    Double dMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_person);
        setControl();
        setEvent();
    }

    private void setEvent() {
        dataTest();
        setListItem();
        setInformationUser();
    }

    private void setInformationUser() {
        tvEmail.setText(PersonAPI.getInstance().getEmail());
        tvMoney.setText(PersonAPI.getInstance().getMoney());
        tvName.setText(PersonAPI.getInstance().getName());
    }

    private void setControl() {
        tvEmail = findViewById(R.id.person_tv_email);
        tvMoney = findViewById(R.id.person_tv_money);
        tvName = findViewById(R.id.person_tv_name);
        recyclerView = findViewById(R.id.person_recyclerView);
    }

    private void dataTest () {
        PersonAPI.getInstance().setName("Cao Van Chien");
        PersonAPI.getInstance().setEmail("chiencao@me.cc");
        PersonAPI.getInstance().setPoint(1000000);
        PersonAPI.getInstance().setUid("12345resdf2434543");
    }

    private void setListItem() {
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        personItemRecycleViews = new ArrayList<PersonItemMenu>();
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_infomation, "rổng"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_infomation, "rổng"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_infomation, "Cá nhân"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_payment, "Thanh Toan"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_save_money, "Nạp tiền"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_money_out, "Rút tiền"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_logout, "Đăng xuất"));
        personItem = new CustomAdapterPersonMenu(getApplicationContext(), personItemRecycleViews);
        recyclerView.setAdapter(personItem);
        personItem.setItemClickListener(new CustomAdapterPersonMenu.ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                switch (position){
                    case 2:
                        Intent intent = new Intent(getApplicationContext(), PersonInformationActivity.class);
                        intent.putExtra("id_person", "ss");
                        startActivity(intent);
                        break;
                    case 3:
                        PersonDialogPayment payment = new PersonDialogPayment(PersonSettingActivity.this);
                        break;
                    case 4:
                        PersonDialogPurchase dialogPurchase = new PersonDialogPurchase(PersonSettingActivity.this);
                        break;
                    case 5:
                        PersonDialogRefund dialogRefund = new PersonDialogRefund(PersonSettingActivity.this);
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


}