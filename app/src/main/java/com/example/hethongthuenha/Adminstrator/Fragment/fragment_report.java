package com.example.hethongthuenha.Adminstrator.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hethongthuenha.Model.PhongTro;
import com.example.hethongthuenha.Adapter.ContactAdapter;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class fragment_report extends Fragment {
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

    public fragment_report() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        PhongTroArrayList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        roomName = view.findViewById(R.id.txtTenPhong);
        roomPrice = view.findViewById(R.id.txtGia);
        btnAdd = view.findViewById(R.id.btnAdd);
        contactView = view.findViewById(R.id.ContactView);
        contactView.setHasFixedSize(true);
        contactView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        btnAdd.setOnClickListener(v -> {
            String strRoomName = "", strRoomPrice = "";

            strRoomName = roomName.getText().toString();
            strRoomPrice = roomPrice.getText().toString();
            addContact(strRoomName, strRoomPrice);
            PhongTro phongtro = new PhongTro(strRoomName, strRoomPrice);
            ref.add(phongtro).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getContext(), "Data Added@", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
    }

    private void Init() {
        ShowDialogReport();
    }

    public void addContact(String strRoomName, String strRoomPrice) {
        PhongTro obj = new PhongTro();
        obj.setRoomName(strRoomName);
        obj.setRoomPrice(strRoomPrice);
        PhongTroArrayList.add(obj);
        contactAdapterList = new ContactAdapter(getContext(), PhongTroArrayList);
        contactView.setAdapter(contactAdapterList);

    }

    public void ShowDialogReport() {
        btnBaoCao.setOnClickListener(v ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View viewLayout = getLayoutInflater().inflate(R.layout.layout_report, null);
            builder.setView(viewLayout);
            spReport = viewLayout.findViewById(R.id.sp_report);
            et_description_report = viewLayout.findViewById(R.id.et_description_report);
            btnCancelReport = viewLayout.findViewById(R.id.btn_cancel_report);
            btnSendReport = viewLayout.findViewById(R.id.btn_send_report);
            btnBaoCao = viewLayout.findViewById(R.id.btnBaoCao);
            final AlertDialog show = builder.show();
            btnCancelReport.setOnClickListener(c -> show.dismiss());
            btnSendReport.setOnClickListener(c -> {
                String typeReport = spReport.getSelectedItem().toString();
                String description = et_description_report.getText().toString();
                Timestamp reportAdded = new Timestamp(new Date());
            });

        });

    }
}