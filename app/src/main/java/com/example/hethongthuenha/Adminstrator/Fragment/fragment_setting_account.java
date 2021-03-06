package com.example.hethongthuenha.Adminstrator.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.Adapter.MemberRecyclerView;
import com.example.hethongthuenha.Adapter.SettingMemberRecyclerView;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class fragment_setting_account extends Fragment {

    private RecyclerView recyclerView;
    private SettingMemberRecyclerView adapter;
    private List<Person> persons;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Xin đợi 1 lát");
        progressDialog.show();
        View view = inflater.inflate(R.layout.fragment_setting_account, container, false);
        recyclerView = view.findViewById(R.id.accountRecyclerview);
        persons = new ArrayList<>();
        adapter = new SettingMemberRecyclerView(getActivity(), persons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        db.collection("User")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error==null){
                            persons.clear();
                            for (QueryDocumentSnapshot v:value){
                                Person person = v.toObject(Person.class);
                                persons.add(person);
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }
                });


        return view;
    }
}