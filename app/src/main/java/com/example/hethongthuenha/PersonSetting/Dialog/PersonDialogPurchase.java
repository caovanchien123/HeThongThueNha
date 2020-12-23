package com.example.hethongthuenha.PersonSetting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Model.NotifyPayment;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;

public class PersonDialogPurchase extends Dialog {
    private TextView tvName, tvEmail, tvID, tvPrice;
    private EditText edt_Price;
    private Button btnCancle, btnYes;
    PersonAPI personAPI = PersonAPI.getInstance();

    public PersonDialogPurchase(@NonNull Context context) {
        super(context);
        setContentView(R.layout.person_dialog_purchase);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_Price.getText().toString().equals("")){
                    if(Double.parseDouble(edt_Price.getText().toString()) >= 50000.0){
                        FirebaseFirestore.getInstance().collection("NotifyPayment").add(new NotifyPayment(personAPI.getUid(), Double.parseDouble(edt_Price.getText().toString()),  "NapTien", false)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Gửi yêu cầu thành công", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Gửi yêu cầu thất bại", Toast.LENGTH_LONG).show();
                            }
                        });
                        cancel();
                    }else{
                        Toast.makeText(getContext(), "Bạn phải nhập số tiền lớn hơn hoặc bằng 50.000 VND", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Bạn chưa nhập dữ liệu", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setControl() {
        tvEmail = findViewById(R.id.tv_email);
        tvID = findViewById(R.id.tv_id);
        tvName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);

        edt_Price = findViewById(R.id.edt_purchase);

        btnYes = findViewById(R.id.btn_yes);
        btnCancle = findViewById(R.id.btn_cancle);
    }

    private void setData(){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        tvEmail.setText(PersonAPI.getInstance().getEmail());
        tvID.setText(PersonAPI.getInstance().getUid());
        tvPrice.setText(formatter.format(PersonAPI.getInstance().getPoint()));
        tvName.setText(PersonAPI.getInstance().getName());
    }
}
