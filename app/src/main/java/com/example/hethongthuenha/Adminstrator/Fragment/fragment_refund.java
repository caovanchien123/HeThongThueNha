package com.example.hethongthuenha.Adminstrator.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.Adapter.RefundRecyclerView;
import com.example.hethongthuenha.Model.Refund;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;


public class fragment_refund extends Fragment {

    private RecyclerView recyclerView;
    private RefundRecyclerView adapter;
    private List<Refund> refunds;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    public fragment_refund() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund, container, false);
        refunds = new ArrayList<>();
        recyclerView = view.findViewById(R.id.refundRecyclerview);
        adapter = new RefundRecyclerView(getActivity(), refunds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Vui lòng đợi một lát");
        progressDialog.show();

        db.collection("Refund").orderBy("refundAdded", Query.Direction.DESCENDING).addSnapshotListener(
                (v, e) -> {
                    if (e == null) {
                        refunds.clear();
                        for (QueryDocumentSnapshot value : v) {
                            Refund refund = value.toObject(Refund.class);
                            refunds.add(refund);
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });

        return view;
    }
}