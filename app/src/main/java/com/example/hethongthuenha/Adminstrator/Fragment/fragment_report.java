package com.example.hethongthuenha.Adminstrator.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hethongthuenha.Adapter.ReportRecyclerView;
import com.example.hethongthuenha.Model.Report;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class fragment_report extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ReportRecyclerView adapter;
    private List<Report> reports;
    private ProgressDialog progressDialog;

    public fragment_report() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        recyclerView = view.findViewById(R.id.reportRecyclerview);
        reports = new ArrayList<>();
        adapter = new ReportRecyclerView(getActivity(), reports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Xin vui lòng chờ");
        progressDialog.show();

        db.collection("Report").get()
                .addOnCompleteListener(v -> {
                    if (v.isSuccessful()) {
                        for (QueryDocumentSnapshot value : v.getResult()) {
                            Report report = value.toObject(Report.class);
                            reports.add(report);
                        }

                        if (v.isComplete()) {
                            adapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                    }
                });

        return view;
    }
}