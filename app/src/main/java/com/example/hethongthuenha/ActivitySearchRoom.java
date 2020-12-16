package com.example.hethongthuenha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.GetDataProvinceService;
import com.example.hethongthuenha.Model.Province;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;


public class ActivitySearchRoom extends AppCompatActivity {


    private GetDataProvinceService service;
    private ArrayList<String> provinces;
    private ArrayAdapter<String> provinceArrayAdapter;
    private Spinner spLocation;
    private RecyclerView recyclerView;
    private EditText edTextSearch;
    private Button btn_search_enhance, btn_finish_search, btnPrice, btnAccommodation, btnArea, btnTypeRoom;
    private TextView tvEmpty;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_room);
        Init();
        InitProvince();
    }

    private void Init() {
        spLocation = findViewById(R.id.sp_search_location);
        edTextSearch = findViewById(R.id.etSearchRoom);
        linearLayout = findViewById(R.id.linearLayoutSearchRoom);
        btn_search_enhance = findViewById(R.id.btn_search_enhance);
        btn_finish_search = findViewById(R.id.btn_finish_search_room);
        btnPrice = findViewById(R.id.btn_price_search_room);
        btnAccommodation = findViewById(R.id.btn_accommodation_search_room);
        btnArea = findViewById(R.id.btn_area_search_room);
        btnTypeRoom = findViewById(R.id.btn_type_room_search_room);
        tvEmpty = findViewById(R.id.tv_empty_search_room);
    }
    public void InitProvince() {
        service.getAllProvince().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BlockingBaseObserver<List<Province>>() {
                    @Override
                    public void onNext(List<Province> value) {
                        provinces.add("Toàn quốc");
                        for (Province province : value) {
                            String splitProvince = "";
                            if (province.getName().contains("Thành phố"))
                                splitProvince = province.getName().split("Thành phố")[1];
                            else if (province.getName().contains("Tỉnh"))
                                splitProvince = province.getName().split("Tỉnh")[1];
                            provinces.add(splitProvince);
                        }


                        provinceArrayAdapter = new ArrayAdapter<String>(ActivitySearchRoom.this,
                                android.R.layout.simple_spinner_dropdown_item, provinces);
                        spLocation.setAdapter(provinceArrayAdapter);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ActivitySearchRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}