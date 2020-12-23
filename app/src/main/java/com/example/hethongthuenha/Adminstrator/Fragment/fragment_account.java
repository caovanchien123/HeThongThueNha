package com.example.hethongthuenha.Adminstrator.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.Adapter.AccountRecyclerView;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class fragment_account extends Fragment {

    private RecyclerView recyclerView;
    private AccountRecyclerView adapter;
    private List<Person> persons;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    public fragment_account() {
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
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Xin đợi 1 lát");
        progressDialog.show();
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        recyclerView = view.findViewById(R.id.accountRecyclerview);
        persons = new ArrayList<>();
        adapter = new AccountRecyclerView(getActivity(), persons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        db.collection("User").whereEqualTo("type_person", 1)
                .get().addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                for (QueryDocumentSnapshot value : v.getResult()) {
                    Person person = value.toObject(Person.class);
                    persons.add(person);
                }
                if (v.isComplete()){
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }
}