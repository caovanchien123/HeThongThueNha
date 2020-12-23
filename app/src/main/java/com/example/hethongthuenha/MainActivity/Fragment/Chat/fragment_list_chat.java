package com.example.hethongthuenha.MainActivity.Fragment.Chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Adapter.PersonChatRecyclerView;
import com.example.hethongthuenha.Model.HistoryChat;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_list_chat extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PersonChatRecyclerView adapter;
    private RecyclerView recyclerView;
    private List<Map<String, Object>> personChats;

    public fragment_list_chat() {
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
        View view = inflater.inflate(R.layout.fragment_list_chat, container, false);
        personChats = new ArrayList<>();
        CollectionReference refHistoryChat = db.collection("History-chat");
        CollectionReference refUser = db.collection("User");
        adapter = new PersonChatRecyclerView(getActivity(), personChats);
        recyclerView = view.findViewById(R.id.personChatRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        refHistoryChat.orderBy("chatAdded", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error==null){
                    personChats.clear();
                    for (QueryDocumentSnapshot v:value){
                        HistoryChat historyChat = v.toObject(HistoryChat.class);
                        String checkUser = historyChat.getPathChat().substring(5, historyChat.getPathChat().length() - 1);
                        if (checkUser.contains(PersonAPI.getInstance().getEmail())) {
                            String[] emails = checkUser.split("-");
                            String[] names = historyChat.getFromATo().split("-");
                            String getEmailPerson = "";
                            String getNamePerson = "";
                            Map<String, Object> maps = new HashMap<>();


                            if (emails[0].equals(PersonAPI.getInstance().getEmail())) {
                                getEmailPerson = emails[1];
                                getNamePerson = names[1];

                            } else {
                                getEmailPerson = emails[0];
                                getNamePerson = names[0];
                            }


                            maps.put("toName", getNamePerson);
                            maps.put("toEmail", getEmailPerson);
                            maps.put("lastChat", historyChat.getLastChat());
                            maps.put("chatAdded",historyChat.getChatAdded());
                            personChats.add(maps);

                        }
                    }
                    adapter.notifyDataSetChanged();

                    if (personChats.size() == 0)
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });


        return view;
    }
}