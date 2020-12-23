package com.example.hethongthuenha.MainActivity.Fragment.Notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Adapter.NotficationRecyclerView;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class fragment_notification extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NotficationRecyclerView adapter;
    private RecyclerView recyclerView;
    private List<Notification> notifications;

    public fragment_notification() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notifications = new ArrayList<>();
        recyclerView = view.findViewById(R.id.notificationRecyclerview);
        adapter = new NotficationRecyclerView(getActivity(), notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        db.collection("Notification").whereEqualTo("id_person_created", PersonAPI.getInstance().getUid())
                .addSnapshotListener((value, error) -> {
                    notifications.clear();
                    if (error == null) {
                        for (QueryDocumentSnapshot v : value) {
                            notifications.add(v.toObject(Notification.class));
                        }

                        Collections.sort(notifications, (o1, o2) -> {
                            return (int) (o2.getNotificationAdded().getSeconds() - o1.getNotificationAdded().getSeconds());
                        });
                        adapter.notifyDataSetChanged();

                        if(notifications.size()==0)
                            recyclerView.setVisibility(View.GONE);
                    }
                });

        return view;
    }
}