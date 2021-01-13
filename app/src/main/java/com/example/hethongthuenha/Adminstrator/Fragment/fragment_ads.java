package com.example.hethongthuenha.Adminstrator.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hethongthuenha.Adapter.AdsRecyclerView;
import com.example.hethongthuenha.Model.Ads;
import com.example.hethongthuenha.Model.Refund;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class fragment_ads extends Fragment {

    private List<Ads> adsList;
    private AdsRecyclerView adapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    public fragment_ads() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads, container, false);
        recyclerView = view.findViewById(R.id.adsRecyclerview);
        adsList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Đang chờ tải !");
        adapter = new AdsRecyclerView(getActivity(), adsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        progressDialog.show();
        db.collection("Ads").orderBy("count_down").addSnapshotListener((v, e) -> {
            if (e == null) {
                adsList.clear();
                for (QueryDocumentSnapshot value : v) {
                    Ads ads = value.toObject(Ads.class);
                    adsList.add(ads);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        return view;
    }
}