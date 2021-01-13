package com.example.hethongthuenha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Adapter.RoomRecyclerView;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Room;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityPerson extends AppCompatActivity {
    private String person_id;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvName, tvContact;
    private RecyclerView recyclerView;
    private ImageView imgAvatar;
    private Button btnContact;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        person_id = getIntent().getStringExtra("id_person");

        Init();
    }

    private void Init() {
        tvName = findViewById(R.id.tv_name_person);
        tvContact = findViewById(R.id.tv_contact_person);
        recyclerView = findViewById(R.id.room_recycler_person);
        imgAvatar = findViewById(R.id.img_avatar_list_room);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnContact = findViewById(R.id.btn_contact);
        LoadInformPerson(person_id);
        LoadRoom(person_id);
        GoToChat();
    }


    private void LoadInformPerson(String person_id) {
        db.collection("User").whereEqualTo("uid", person_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    person = documentSnapshot.toObject(Person.class);
                    tvName.setText(person.getFullName());
                    tvContact.setText("SĐT:" + person.getContact());

                    if (!person.getUrl().equals("")) {
                        Picasso.with(ActivityPerson.this)
                                .load(person.getUrl()).placeholder(R.drawable.ic_baseline_person_24)
                                .into(imgAvatar);
                    }
                }
            }
        });
    }

    private void GoToChat() {

        if (person_id.equals(PersonAPI.getInstance().getUid()))
            btnContact.setVisibility(View.GONE);
        btnContact.setOnClickListener(v -> {
            if (person != null) {
                Intent intent = new Intent(ActivityPerson.this, ActivityChat.class);
                intent.putExtra("toEmail", person.getEmail());
                intent.putExtra("toName", person.getFullName());
                startActivity(intent);
            }

        });
    }

    private void LoadRoom(String person_id) {
        List<Room> rooms = new ArrayList<>();
        RoomRecyclerView adapter = new RoomRecyclerView(this, rooms);
        recyclerView.setAdapter(adapter);
        db.collection("Room").whereEqualTo("person_id", person_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Room room = documentSnapshot.toObject(Room.class);
                rooms.add(room);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.d(this.getClass().getName(), "Person: " + e.getMessage()));
    }
}