package com.example.hethongthuenha.Adminstrator.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class fragment_commission extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvTotalPerson, tvTotalRoom, tvCommission;
    private Map<String, Double> commission;
    private Button btnFinish;
    private EditText edCommission;

    public fragment_commission() {
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
        commission = new HashMap<>();
        View view = inflater.inflate(R.layout.fragment_commission, container, false);
        btnFinish = view.findViewById(R.id.btnCommission);
        edCommission = view.findViewById(R.id.etCommission);
        tvCommission = view.findViewById(R.id.tv_number_commission);
        tvTotalPerson = view.findViewById(R.id.tv_totalPerson_commission);
        tvTotalRoom = view.findViewById(R.id.tv_totalRoom_commission);

        db.collection("Room").get().addOnCompleteListener(v -> {
            int countRoom = 0;
            if (v.isSuccessful()) {
                for (QueryDocumentSnapshot value : v.getResult()) {
                    countRoom++;
                }
                if (v.isComplete())
                    tvTotalRoom.setText("Tất cả phòng trọ:" + countRoom);
            }
        });

        db.collection("User").get().addOnCompleteListener(v -> {
            int countPerson = 0;
            if (v.isSuccessful()) {
                for (QueryDocumentSnapshot value : v.getResult()) {
                    countPerson++;
                }
                if (v.isComplete())
                    tvTotalPerson.setText("Tất cả người dùng:" + countPerson);
            }
        });

        db.collection("Scale_Commission").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    for (QueryDocumentSnapshot v : value) {
                        tvCommission.setText("Hoa hồng hiện tại:" + v.get("commission"));
                    }
                }
            }
        });

        btnFinish.setOnClickListener(v -> {
            commission.put("commission", Double.parseDouble(edCommission.getText().toString()));

            db.collection("Scale_Commission").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                                db.collection("Scale_Commission").document(value.getId())
                                        .set(commission);
                            }
                        } else {
                            db.collection("Scale_Commission").add(commission);
                        }
                        Toast.makeText(getActivity(), "Cập nhật thành công",
                                Toast.LENGTH_SHORT).show();
                        edCommission.setText("");
                    });
        });

        return view;
    }
}