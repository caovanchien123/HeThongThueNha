package com.example.hethongthuenha.PersonSetting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.R;

public class DialogPayment extends Dialog {
    Button btn_ThanhToan, btn_Huy;
    TextView tv_Money, tv_Detail, tv_Name, tv_ID, tv_Email;
    String sMoney, sDetail, sName, sID, sEmail;

    public DialogPayment(@NonNull Context context) {
        super(context);
        setContentView(R.layout.person_dialog_payment);
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setControl();
        setData();
        setEvent();
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void setEvent() {
        tv_Detail.setText(sDetail);
        tv_Money.setText(sMoney);
        tv_Name.setText(sName);
        tv_ID.setText(sID);
        tv_Email.setText(sEmail);
    }

    private void setControl() {
        btn_Huy = findViewById(R.id.btn_Huy);
        tv_Detail = findViewById(R.id.tv_Detail);
        tv_Money = findViewById(R.id.tv_Price);
        tv_Name = findViewById(R.id.tv_Name);
        tv_ID = findViewById(R.id.tv_ID);
        tv_Email = findViewById(R.id.tv_Email);
    }

    private void setData(){
        sDetail = "Thanh toan tien";
        sEmail = PersonAPI.getInstance().getEmail();
        sID = PersonAPI.getInstance().getUid();
        sMoney = PersonAPI.getInstance().getPoint()+"";
        sName = PersonAPI.getInstance().getName();
    }
}
