package com.example.hethongthuenha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.hethongthuenha.Adapter.RoomRecyclerView;
import com.example.hethongthuenha.Model.Province;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.Retrofit.RetrofitClientInstance;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;


public class ActivitySearchRoom extends AppCompatActivity {


    //firebasefirestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference refRoom = db.collection("Room");
    //component
    private RoomRecyclerView adapter;
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private EditText edTextSearch;
    private LinearLayout linearLayout;
    private Button btn_search_enhance, btn_finish_search, btnPrice, btnAccommodation, btnArea, btnTypeRoom;
    //progressbar
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    //list room
    private List<Room> rooms;
    //retrofit province
    private GetDataProvinceService service;
    private ArrayAdapter<String> provinceArrayAdapter;
    private ArrayList<String> provinces;
    private Spinner spLocation;

    //format money
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    //variable
    private boolean isOpen = false;
    private String titleRoom, locationRoom, typeRoom;
    private float price, area;
    private int accommodation;
    //bound
    private final int SEARCH_DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_room);
        Init();
        InitProvince();
    }

    private void Init() {
        recyclerView = findViewById(R.id.search_roomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang chờ tìm phòng");
        progressDialog.show();
        rooms = new ArrayList<>();
        edTextSearch = findViewById(R.id.etSearchRoom);
        linearLayout = findViewById(R.id.linearLayoutSearchRoom);
        btn_search_enhance = findViewById(R.id.btn_search_enhance);
        btn_finish_search = findViewById(R.id.btn_finish_search_room);
        btnPrice = findViewById(R.id.btn_price_search_room);
        btnAccommodation = findViewById(R.id.btn_accommodation_search_room);
        btnArea = findViewById(R.id.btn_area_search_room);
        btnTypeRoom = findViewById(R.id.btn_type_room_search_room);
        tvEmpty = findViewById(R.id.tv_empty_search_room);
        adapter = new RoomRecyclerView(this, rooms);
        provinces = new ArrayList<>();
        spLocation = findViewById(R.id.sp_search_location);
        recyclerView.setAdapter(adapter);
        titleRoom = getIntent().getExtras().getString("search_title", "");
        edTextSearch.setText(titleRoom);
        SearchRoom(titleRoom);
        this.service = RetrofitClientInstance.getRetrofitInstance().create(GetDataProvinceService.class);
        DialogSearchEnhance();
        OnClickSearchRoom();
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


                        provinceArrayAdapter = new ArrayAdapter<>(ActivitySearchRoom.this,
                                android.R.layout.simple_spinner_dropdown_item, provinces);
                        spLocation.setAdapter(provinceArrayAdapter);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ActivitySearchRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void handleSearchEnhance() {
        locationRoom = spLocation.getCount() == 0 || spLocation.getSelectedItem().toString() == "Toàn quốc"
                ? "" : spLocation.getSelectedItem().toString();

        //parse format price
        try {
            price = (Long) formatter.parse(btnPrice.getText().toString());
        } catch (ParseException e) {
            price = SEARCH_DEFAULT;
        }

        //accommodation
        accommodation = btnAccommodation.getText().toString().equals("Sức chứa")
                || btnAccommodation.getText().toString().equals("Tất cả") ? SEARCH_DEFAULT :
                Integer.parseInt(btnAccommodation.getText().toString());

        //area
        area = btnArea.getText().toString().equals("Diện tích") ||
                btnArea.getText().toString().equals("Tất cả") ? SEARCH_DEFAULT : Float.parseFloat(btnArea.getText()
                .toString().split("m2")[0]);

        //type_room
        typeRoom = btnTypeRoom.getText().toString().equals("Loại phòng")
                || btnTypeRoom.getText().toString().equals("Tất cả") ? "" :
                btnTypeRoom.getText().toString().toLowerCase();
    }

    public void InitRoom(String keyword) {

        handleSearchEnhance();
        refRoom.orderBy("timeAdded", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            rooms.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Room room = documentSnapshot.toObject(Room.class);

                String title = keyword.toLowerCase();
                String location = locationRoom.toLowerCase();

                if (room.getStage1().getTitle().toLowerCase().contains(title) &&
                        room.getStage1().getAddress().toLowerCase().contains(location) &&
                        room.getStage1().getType_room().toLowerCase().contains(typeRoom)) {
                    if (price == SEARCH_DEFAULT)
                        price = Float.MAX_VALUE;

                    if (accommodation == SEARCH_DEFAULT)
                        accommodation = Integer.MAX_VALUE;

                    if (area == SEARCH_DEFAULT)
                        area = Float.MAX_VALUE;

                    if (room.getStage1().getAccommodation() <= accommodation &&
                            room.getStage1().getPrice() <= price &&
                            room.getStage1().getArea() <= area)
                        rooms.add(room);
                }

            }

            if (rooms.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
            }

            adapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }).addOnFailureListener(e -> Log.d(this.getClass().getName(), "InitRoom: " + e.getMessage()));
    }

    private void DialogSearchEnhance() {
        btnPrice.setOnClickListener(v -> {
            DialogPrice();
        });

        btnTypeRoom.setOnClickListener(v -> {
            DialogTypeRoom();
        });

        btnArea.setOnClickListener(v -> {
            DialogArea();
        });

        btnAccommodation.setOnClickListener(v -> {
            DialogAccommodation();
        });
    }

    private void OnClickSearchRoom() {
        btn_search_enhance.setOnClickListener(v -> {
            PopupSearchEnhance();
        });

        btn_finish_search.setOnClickListener(v -> {
            SearchRoom(edTextSearch.getText().toString());
            PopupSearchEnhance();
            ClearSearch();
        });
        //keyword search
        edTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    SearchRoom(v.getText().toString());
                    return true; // consume.
                }
            }
            return false; // pass on to other listeners.
        });
    }


    private void PopupSearchEnhance() {
        isOpen = !isOpen;
        if (isOpen) {
            linearLayout.setVisibility(View.VISIBLE);
            //https://stackoverflow.com/questions/22297073/how-to-programmatically-set-drawableright-on-android-edittext
            btn_search_enhance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
        } else {
            linearLayout.setVisibility(View.GONE);
            btn_search_enhance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
        }
    }

    private void ClearSearch() {
        spLocation.setSelection(0);
        btnAccommodation.setText("Sức chứa");
        btnArea.setText("Diện tích");
        btnPrice.setText("Giá");
        btnTypeRoom.setText("Loại phòng");
    }

    private void SearchRoom(String keyword) {
        progressDialog.show();
        InitRoom(keyword);
    }


    private void DialogPrice() {
        builder = new AlertDialog.Builder(this);

        builder.setTitle("Giá tiền");
        ArrayAdapter<String> prices = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        prices.add("Tất cả");
        for (int i = 0; i < 100; i++) {
            prices.add(formatter.format(500000 + (500000 * i)));
        }
        builder.setAdapter(prices, (dialog, which) -> {
            btnPrice.setText(prices.getItem(which));
        });
        AlertDialog dialog = builder.show();
    }

    private void DialogAccommodation() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Sức chứa");
        ArrayAdapter<String> accommodation = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        accommodation.add("Tất cả");
        for (int i = 1; i < 10; i++) {
            accommodation.add("" + i);
        }
        builder.setAdapter(accommodation, (dialog, which) -> {
            btnAccommodation.setText(accommodation.getItem(which));
        });
        AlertDialog dialog = builder.show();
    }

    private void DialogArea() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Diện tích");
        ArrayAdapter<String> areas = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        areas.add("Tất cả");
        for (int i = 1; i < 40; i++) {
            areas.add((10) * i + "m2");
        }
        builder.setAdapter(areas, (dialog, which) -> {
            btnArea.setText(areas.getItem(which));
        });
        AlertDialog dialog = builder.show();
    }

    private void DialogTypeRoom() {
        builder = new AlertDialog.Builder(this);

        builder.setTitle("Kiểu phòng");
        ArrayAdapter<String> type_room = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        type_room.add("Tất cả");
        type_room.add("Phòng trọ");
        type_room.add("Nhà nguyên căn");
        type_room.add("Ở ghép");

        builder.setAdapter(type_room, (dialog, which) -> {
            btnTypeRoom.setText(type_room.getItem(which));
        });
        AlertDialog dialog = builder.show();

    }

}