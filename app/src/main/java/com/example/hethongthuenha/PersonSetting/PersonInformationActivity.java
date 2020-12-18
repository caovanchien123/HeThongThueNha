package com.example.hethongthuenha.PersonSetting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.ModelA.Room;
import com.example.hethongthuenha.PersonSetting.Adapter.CustomAdapterPersonInformation;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PersonInformationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Room> roomArrayList;
    CustomAdapterPersonInformation customAdapterPersonInformation;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation_person_setting);

        setControl();
        setEvent();
    }

    private void setEvent() {
        getData();
    }

    private void getData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        roomArrayList = new ArrayList<Room>();
        firestore.collection("Room").whereEqualTo("person_id", "KV1AuttZUFXmaJCjJ8pHHYP2VwD2").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    roomArrayList.add(snapshot.toObject(Room.class));
                }
                customAdapterPersonInformation.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "LoidDatabase", Toast.LENGTH_SHORT).show();
            }
        });
        customAdapterPersonInformation = new CustomAdapterPersonInformation(PersonInformationActivity.this, roomArrayList);
        recyclerView.setAdapter(customAdapterPersonInformation);
    }

    private void setControl() {
        recyclerView = findViewById(R.id.person_information_home);
    }
}
