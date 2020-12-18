package com.example.hethongthuenha.PersonSetting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hethongthuenha.ModelA.Room;
import com.example.hethongthuenha.R;

import androidx.annotation.NonNull;

public class PersonDialogMenuItem extends Dialog {
    LinearLayout lnXoa, lnSua, lnQC, lnDSNguoiDung;
    Context context;
    Room room;
    public PersonDialogMenuItem(@NonNull Context context, Room room) {
        super(context);
        setContentView(R.layout.person_information_dialog_item_menu);
        this.room = room;
        this.context = context;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lnDSNguoiDung = findViewById(R.id.person_information_NguoiThue);
        lnXoa = findViewById(R.id.person_information_delete);
        lnSua = findViewById(R.id.person_information_edit);
        lnQC = findViewById(R.id.person_information_qc);
        setEven();
    }

    private void setEven() {
        lnDSNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Danh Sach Nguoi dung", Toast.LENGTH_LONG).show();
            }
        });
        lnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Xoa", Toast.LENGTH_LONG).show();
            }
        });
        lnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Sua", Toast.LENGTH_LONG).show();
            }
        });
        lnQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "QC", Toast.LENGTH_LONG).show();
            }
        });
    }




}
