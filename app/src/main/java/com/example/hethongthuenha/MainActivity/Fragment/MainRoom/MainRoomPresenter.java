package com.example.hethongthuenha.MainActivity.Fragment.MainRoom;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.hethongthuenha.Model.Room;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainRoomPresenter implements MainRoomContract.Presenter {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MainRoomContract.View view;

    public MainRoomPresenter(MainRoomContract.View view) {
        this.view = view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    public void InitRoom() {
        List<Room> rooms = new ArrayList<>();
        db.collection("Room")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Room room = documentSnapshot.toObject(Room.class);
                rooms.add(room);
            }

            Collections.sort(rooms, (o1, o2) -> {
                if (o1.getOrder() == o2.getOrder())
                    return 0;
                else if (o1.getOrder() > o2.getOrder())
                    return 1;
                else
                    return -1;
            });

            Collections.sort(rooms, (o1, o2) -> {
                if (o1.getOrder() == o2.getOrder())
                    return (o2.getTimeAdded().compareTo(o1.getTimeAdded()));
                else
                    return -1;
            });

            view.InitAdapter(rooms);
        }).addOnFailureListener(e -> Log.d(this.getClass().getName(), "InitRoom: " + e.getMessage()));
    }
}
