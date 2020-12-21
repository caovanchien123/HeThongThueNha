package com.example.hethongthuenha.PersonSetting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.R;

public class PersonDialogPayment extends Dialog {
    Button btn_ThanhToan, btn_Huy;
    TextView tv_Money, tv_Detail, tv_Name, tv_ID, tv_Email;

    public PersonDialogPayment(@NonNull Context context) {
        super(context);
        setContentView(R.layout.person_dialog_payment);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setControl();
        setData();
        setEvent();
    }

    private void setEvent() {
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        btn_ThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setControl() {
        btn_Huy = findViewById(R.id.btn_Huy);
        tv_Detail = findViewById(R.id.tv_Detail);
        tv_Money = findViewById(R.id.tv_Price);
        tv_Name = findViewById(R.id.tv_Name);
        tv_ID = findViewById(R.id.tv_ID);
        tv_Email = findViewById(R.id.tv_Email);
        btn_ThanhToan = findViewById(R.id.btn_ThanhToan);
    }

    private void setData(){
        tv_Detail.setText("Thanh toán hoa hồng");
        tv_Money.setText(PersonAPI.getInstance().getMoney());
        tv_Name.setText(PersonAPI.getInstance().getName());
        tv_ID.setText(PersonAPI.getInstance().getUid());
        tv_Email.setText(PersonAPI.getInstance().getEmail());
    }
}
