package com.example.hethongthuenha.Adminstrator.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.Adapter.HistoryCreditRecyclerView;
import com.example.hethongthuenha.Model.HistoryCreditCard;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class fragmment_history_credit extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<HistoryCreditCard> hCards;
    private HistoryCreditRecyclerView adapter;
    private RecyclerView recyclerView;

    public fragmment_history_credit() {
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
        View view = inflater.inflate(R.layout.fragment_history_credit, container, false);
        hCards = new ArrayList<>();
        recyclerView = view.findViewById(R.id.history_credit_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryCreditRecyclerView(getActivity(), hCards);
        recyclerView.setAdapter(adapter);

        db.collection("History-CreditCard").orderBy("creditCardAdded", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    hCards.add(value.toObject(HistoryCreditCard.class));
                }
                if (task.isComplete())
                    adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}