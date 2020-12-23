package com.example.hethongthuenha.CreateRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.CreateRoom.Fragment.IDataCommunication;
import com.example.hethongthuenha.CreateRoom.Fragment.fragment_description;
import com.example.hethongthuenha.CreateRoom.Fragment.fragment_image;
import com.example.hethongthuenha.CreateRoom.Fragment.fragment_living_expenses;
import com.example.hethongthuenha.CreateRoom.Fragment.fragment_utilities;
import com.example.hethongthuenha.MainActivity.MainActivity;
import com.example.hethongthuenha.Model.Description_Room;
import com.example.hethongthuenha.Model.District;
import com.example.hethongthuenha.Model.Image_Room;
import com.example.hethongthuenha.Model.LivingExpenses_Room;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.Model.Utilities_Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class CreateRoomActivity extends AppCompatActivity implements IDataCommunication {

    private TextView tvStage1, tvStage2, tvStage3, tvStage4;
    private ImageView imgStage1, imgStage2, imgStage3, imgStage4;
    //private Button btnFinishStage;
    private Toolbar toolbar;
    private TextView[] tvStage = new TextView[4];
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Room room;
    static DocumentReference ref = db.collection("Room").document();
    public static Room roomExist;
    public static final String myID = ref.getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        init();
        setFragment();
    }


    private void init() {
        tvStage1 = findViewById(R.id.tvStage1);
        tvStage2 = findViewById(R.id.tvStage2);
        tvStage3 = findViewById(R.id.tvStage3);
        tvStage4 = findViewById(R.id.tvStage4);

        for (int i = 0; i < 4; i++) {
            String textViewStageId = "tvStage" + (i + 1);
            int resId = getResources().getIdentifier(textViewStageId, "id", getPackageName());
            tvStage[i] = findViewById(resId);
        }

        imgStage1 = findViewById(R.id.imgStage1);
        imgStage2 = findViewById(R.id.imgStage2);
        imgStage3 = findViewById(R.id.imgStage3);
        imgStage4 = findViewById(R.id.imgStage4);

        toolbar = findViewById(R.id.toolbar);


        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.mnCancel)
                startActivity(new Intent(CreateRoomActivity.this, MainActivity.class));
            return false;
        });

        room = new Room();

        room.setRoom_id(myID);
        room.setPerson_id(PersonAPI.getInstance().getUid());
        room.setOrder(1);

        String UpdateMyRoom = getIntent().getStringExtra("update");

        if (UpdateMyRoom != null) {
            roomExist = (Room) getIntent().getSerializableExtra("room");
        } else
            roomExist = null;
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameContainer, new fragment_description());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            startActivity(new Intent(CreateRoomActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    @Override
    public void Description(Description_Room dataStage1) {
        room.setStage1(dataStage1);
    }

    @Override
    public void LivingExpenses(LivingExpenses_Room dataStage2) {
        room.setStage2(dataStage2);
    }

    @Override
    public void Image(Image_Room dataStage3) {
        room.setStage3(dataStage3);
    }

    @Override
    public void Utilities(Utilities_Room dataStage4) {
        room.setStage4(dataStage4);
        room.setTimeAdded(new Timestamp(new Date()));
        if (roomExist == null) {
            db.collection("Room").add(room)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(CreateRoomActivity.this, "Tạo thành công", Toast.LENGTH_SHORT).show()).
                    addOnFailureListener(e ->
                            Toast.makeText(CreateRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else if (roomExist != null) {
            room.setRoom_id(roomExist.getRoom_id());
            room.setTimeAdded((Timestamp) getIntent().getExtras().get("roomAdded"));
            db.collection("Room")
                    .whereEqualTo("room_id", room.getRoom_id())
                    .get().addOnCompleteListener(v -> {
                if (v.isSuccessful()) {
                    for (QueryDocumentSnapshot value : v.getResult()) {
                        Log.d("Test", "Utilities: " + value.getId() + "||" + room.getRoom_id());
                        db.collection("Room").document(value.getId())
                                .set(room).addOnCompleteListener(c -> {
                            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }

    }
}