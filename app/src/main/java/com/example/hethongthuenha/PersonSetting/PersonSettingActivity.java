package com.example.hethongthuenha.PersonSetting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.Model.PersonItemMenu;
import com.example.hethongthuenha.PersonSetting.Adapter.CustomAdapterPersonMenu;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PersonSettingActivity extends AppCompatActivity {
    private TextView tvName, tvMoney, tvEmail;
    RecyclerView recyclerView;
    ArrayList<PersonItemMenu> personItemRecycleViews;
    CustomAdapterPersonMenu personItem;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_person);
        setControl();
        setEvent();
    }

    private void setEvent() {
        setListItem();
    }

    private void setControl() {
        tvEmail = findViewById(R.id.person_tv_email);
        tvMoney = findViewById(R.id.person_tv_money);
        tvName = findViewById(R.id.person_tv_name);
        recyclerView = findViewById(R.id.person_recyclerView);
    }

    private void getInformationUser() {

    }

    private void setListItem() {
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        personItemRecycleViews = new ArrayList<PersonItemMenu>();
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_infomation, "Cá nhân"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_payment, "Hoa hồng"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_save_money, "Nạp tiền"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_money_out, "Rút tiền"));
        personItemRecycleViews.add(new PersonItemMenu(R.drawable.person_image_logout, "Đăng xuất"));
        personItem = new CustomAdapterPersonMenu(getApplicationContext(), personItemRecycleViews);
        recyclerView.setAdapter(personItem);
        personItem.setItemClickListener(new CustomAdapterPersonMenu.ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                switch (position){
                    case 0:
                        Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}