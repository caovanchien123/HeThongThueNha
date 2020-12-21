package com.example.hethongthuenha;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Adapter.PhongTro;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ContactAdapter contactAdapterList;
    RecyclerView contactView;
    ArrayList<PhongTro> PhongTroArrayList;
    private EditText roomName, roomPrice, et_description_report;
    private Button btnAdd, btnSendReport, btnCancelReport, btnBaoCao;
    ImageView imgWarning;
    private Spinner spReport;

    //add vao fire base
    CollectionReference ref = db.collection("NhaTro");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PhongTroArrayList = new ArrayList<>();

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        roomName = findViewById(R.id.txtTenPhong);
        roomPrice = findViewById(R.id.txtGia);
        btnAdd = findViewById(R.id.btnAdd);
        contactView = findViewById(R.id.ContactView);
        contactView.setHasFixedSize(true);
        contactView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        btnAdd.setOnClickListener(v -> {
            String strRoomName = "", strRoomPrice = "";

            strRoomName = roomName.getText().toString();
            strRoomPrice = roomPrice.getText().toString();
            addContact(strRoomName, strRoomPrice);
            PhongTro phongtro = new PhongTro(strRoomName, strRoomPrice);
            ref.add(phongtro).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(MainActivity.this, "Data Added@", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }



    public void addContact(String strRoomName, String strRoomPrice) {
        PhongTro obj = new PhongTro();
        obj.setRoomName(strRoomName);
        obj.setRoomPrice(strRoomPrice);
        PhongTroArrayList.add(obj);
        contactAdapterList = new ContactAdapter(this, PhongTroArrayList);
        contactView.setAdapter(contactAdapterList);

    }




}
