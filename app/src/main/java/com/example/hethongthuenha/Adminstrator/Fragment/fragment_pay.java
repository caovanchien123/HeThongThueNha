package com.example.hethongthuenha.Adminstrator.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hethongthuenha.Adapter.PayRecyclerView;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.Commission;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class fragment_pay extends Fragment {

    private RecyclerView recyclerView;
    private PayRecyclerView adapter;
    private List<Commission> commissions;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Person> persons;
    private List<Room> rooms;
    private List<BookRoom> bookRooms;
    private double price = 0;
    private int totalDay = 0;
    private double scale_comission = 0;

    private interface IValueCommission {
        public void getValueCommission(List<Person> persons, List<Room> rooms, List<BookRoom> bookRooms
                , double scale_commission);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        recyclerView = view.findViewById(R.id.payRecyclerview);
        commissions = new ArrayList<>();
        rooms = new ArrayList<>();
        persons = new ArrayList<>();
        bookRooms = new ArrayList<>();
        adapter = new PayRecyclerView(getActivity(), commissions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        ShowCommission();

        return view;
    }

    private void getCommission(IValueCommission ICommission) {
        db.collection("User").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot value : task.getResult()) {
                            Person person = value.toObject(Person.class);
                            persons.add(person);
                        }
                    }
                    if (task.isComplete()) {
                        db.collection("Room")
                                .get().addOnCompleteListener(task1 -> {

                            if (task1.isSuccessful()) {
                                rooms.clear();
                                for (QueryDocumentSnapshot value1 : task1.getResult()) {
                                    Room room = value1.toObject(Room.class);
                                    rooms.add(room);
                                }
                            }
                            if (task1.isComplete()) {
                                db.collection("BookRoom")
                                        .get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        bookRooms.clear();
                                        for (QueryDocumentSnapshot value2 : task2.getResult()) {
                                            BookRoom bookRoom = value2.toObject(BookRoom.class);
                                            bookRooms.add(bookRoom);
                                        }
                                    }
                                    if (task2.isComplete()) {
                                        db.collection("Scale_Commission").get()
                                                .addOnCompleteListener(task3 -> {
                                                    if (task3.isSuccessful()) {
                                                        for (QueryDocumentSnapshot value3 : task3.getResult()) {
                                                            scale_comission = value3.getDouble("commission");
                                                        }
                                                        if (task3.isComplete()) {
                                                            ICommission.getValueCommission(persons, rooms, bookRooms, scale_comission);
                                                        }
                                                    }
                                                });

                                    }
                                });
                            }
                        });
                    }
                });


    }

    private void ShowCommission() {
        getCommission((persons, rooms, bookRooms, scale_commission) -> {
            Commission commission = null;
            for (Person person : persons) {
                commission = new Commission();
                commission.setId_person(person.getUid());
                price = 0;
                totalDay = 0;
                for (Room room : rooms) {
                    if (person.getUid().equals(room.getPerson_id())) {
                        for (BookRoom bookRoom : bookRooms) {
                            if (bookRoom.getId_room().equals(room.getRoom_id()) && bookRoom.isConfirm()) {
                                Calendar calNow = Calendar.getInstance();
                                calNow.setTime(new Date());
                                Calendar calBookRoom = Calendar.getInstance();
                                calBookRoom.setTime(bookRoom.getBookRoomAdded().toDate());
                                int dayWhenAccepted;

                                long diff = calNow.getTime().getTime()-calBookRoom.getTime().getTime();

                                dayWhenAccepted = (int) (diff / (1000 * 60 * 60 * 24));
                                totalDay += dayWhenAccepted;

                                price += ((room.getStage1().getPrice() * scale_commission) / 100 * dayWhenAccepted) / 30;
                            }
                        }
                    }
                }
                commission.setPrice(price);
                commission.setLastPaid(new Timestamp(new Date()));
                commission.setTotalDay(totalDay);
                commissions.add(commission);
            }

            adapter.notifyDataSetChanged();

        });
    }

}