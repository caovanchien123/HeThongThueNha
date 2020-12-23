package com.example.hethongthuenha.CreateRoom.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hethongthuenha.CreateRoom.CreateRoomActivity;
import com.example.hethongthuenha.Model.Description_Room;
import com.example.hethongthuenha.Model.Image_Room;
import com.example.hethongthuenha.Model.LivingExpenses_Room;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.Model.Utilities_Room;
import com.example.hethongthuenha.R;
import com.google.android.material.textfield.TextInputLayout;


public class fragment_description extends Fragment {


    Button btFinishStage1;
    IDataCommunication dataCommunication;
    EditText etTitle, etDescription, etAddress, etPrice, etArea, etAccommodation, etAmout;
    TextInputLayout tipTitle, tipDescription, tipAddress, tipPrice, tipArea, tipAccommodation, tipAmout;
    RadioButton radPhongTro, radNhaNguyenCan, radOGhep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataCommunication = (IDataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etAddress = view.findViewById(R.id.etAddress);
        etPrice = view.findViewById(R.id.etPrice);
        etAccommodation = view.findViewById(R.id.etAccommodation);
        etAmout = view.findViewById(R.id.etAmout);
        etArea = view.findViewById(R.id.etArea);
        btFinishStage1 = view.findViewById(R.id.btnFinishStage1);
        radNhaNguyenCan = view.findViewById(R.id.radNhaNguyenCan);
        radOGhep = view.findViewById(R.id.radOGhep);
        radPhongTro = view.findViewById(R.id.radPhongTro);
        tipTitle = view.findViewById(R.id.filledTitle);
        tipAmout = view.findViewById(R.id.filledAmout);
        tipPrice = view.findViewById(R.id.filledPrice);
        tipAddress = view.findViewById(R.id.filledAddress);
        tipAccommodation = view.findViewById(R.id.filledAccommodation);
        tipArea = view.findViewById(R.id.filledArea);
        tipDescription = view.findViewById(R.id.filledDescription);


        if (CreateRoomActivity.roomExist != null) {
            Room room = CreateRoomActivity.roomExist;

            etTitle.setText(room.getStage1().getTitle());
            etDescription.setText(room.getStage1().getDescription());
            etAddress.setText(room.getStage1().getAddress());
            etPrice.setText("" + room.getStage1().getPrice());
            etArea.setText("" + room.getStage1().getArea());
            etAccommodation.setText("" + room.getStage1().getAccommodation());
            etAmout.setText("" + room.getStage1().getAmout());


            if (room.getStage1().getType_room().equals("Phòng trọ"))
                radPhongTro.setChecked(true);
            else if (room.getStage1().getType_room().equals("Nhà nguyên căn"))
                radNhaNguyenCan.setChecked(true);
            else
                radOGhep.setChecked(true);
        }

        btFinishStage1.setOnClickListener(v -> {

            if (isValid()) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String address = etAddress.getText().toString();
                double price = Double.parseDouble(etPrice.getText().toString());
                double area = Double.parseDouble(etArea.getText().toString());
                int amout = Integer.parseInt(etAmout.getText().toString());
                int accommodation = Integer.parseInt(etAccommodation.getText().toString());
                String typeRoom;
                if (radPhongTro.isChecked())
                    typeRoom = "Phòng trọ";
                else if (radOGhep.isChecked())
                    typeRoom = "Ở ghép";
                else
                    typeRoom = "Nhà nguyên căn";

                Description_Room dataStage1 = new Description_Room
                        (title, description, address, price, area, accommodation, amout, typeRoom);
                dataCommunication.Description(dataStage1);

                fragment_living_expenses fragment = new fragment_living_expenses();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }



    private boolean isValid() {
        boolean valid = true;
        if (TextUtils.isEmpty(etTitle.getText())) {
            etTitle.setError("Làm ơn không bỏ trống");
            valid = false;
        }

        if (TextUtils.isEmpty(etArea.getText())) {
            etArea.setError("Làm ơn không bỏ trống");
            valid = false;
        }
        if (TextUtils.isEmpty(etDescription.getText())) {
            etDescription.setError("Làm ơn không bỏ trống");
            valid = false;
        }
        if (TextUtils.isEmpty(etAccommodation.getText())) {
            etAccommodation.setError("Làm ơn không bỏ trống");
            valid = false;
        }
        if (TextUtils.isEmpty(etAddress.getText())) {
            etAddress.setError("Làm ơn không bỏ trống");
            valid = false;
        }
        if (TextUtils.isEmpty(etAmout.getText())) {
            etAmout.setError("Làm ơn không bỏ trống");
            valid = false;
        }
        if (TextUtils.isEmpty(etPrice.getText())) {
            etPrice.setError("Làm ơn không bỏ trống");
            valid = false;
        }
        return valid;
    }

}