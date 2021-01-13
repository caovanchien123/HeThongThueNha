package com.example.hethongthuenha.Adminstrator.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hethongthuenha.Model.CreditCard;
import com.example.hethongthuenha.Model.HistoryCreditCard;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class fragment_add_point extends Fragment {

    public fragment_add_point() {
        // Required empty public constructor
    }


    private EditText edEmailPerson, edNumberBankCard, edNumberAddPoint;
    private Button btnFinish;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference refCreditCard = db.collection("CreditCard");
    private CollectionReference refHistoryCard = db.collection("History-CreditCard");
    private CollectionReference refNotification = db.collection("Notification");
    private List<Person> persons;
    private ArrayAdapter<String> adapterId, adapterEmail;
    private ArrayList<String> convertIDToAdapter;
    private ArrayList<String> convertEmailToAdapter;
    private Spinner spUID, spEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit, container, false);
        edNumberBankCard = view.findViewById(R.id.ed_bank_credit);
        persons = new ArrayList<>();
        edNumberAddPoint = view.findViewById(R.id.etNumberPointAdded);
        convertIDToAdapter = new ArrayList<String>();
        convertEmailToAdapter = new ArrayList<String>();
        adapterId = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, convertIDToAdapter);
        adapterEmail = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, convertEmailToAdapter);
        btnFinish = view.findViewById(R.id.btnFinishCredit);
        spUID = view.findViewById(R.id.sp_id_person_credit);
        spEmail = view.findViewById(R.id.sp_email_person_credit);
        spUID.setAdapter(adapterId);
        spUID.setEnabled(false);
        spEmail.setAdapter(adapterEmail);
        db.collection("User").orderBy("email").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot value : task.getResult()) {
                            Person person = value.toObject(Person.class);
                            persons.add(person);
                        }
                        if (task.isSuccessful()) {
                            for (Person person : persons) {
                                convertIDToAdapter.add(person.getUid());
                                convertEmailToAdapter.add(person.getEmail());
                            }

                            adapterEmail.notifyDataSetChanged();
                            adapterId.notifyDataSetChanged();
                        }
                    }
                });

        spEmail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spUID.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnFinish.setOnClickListener(v ->
        {
            db.collection("CreditCard").whereEqualTo("email_person", spEmail.getSelectedItem().toString())
                    .whereEqualTo("id_person", spUID.getSelectedItem().toString())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                double point = Double.parseDouble(edNumberAddPoint.getText().toString());
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                        CreditCard card = value.toObject(CreditCard.class);
                        card.setPoint(card.getPoint() + point);
                        card.setNumber_bankcard(edNumberBankCard.getText().toString());
                        refCreditCard.document(value.getId()).set(card);

                    }
                } else {
                    DocumentReference id = refCreditCard.document();
                    CreditCard card = new CreditCard(id.getId(), spUID.getSelectedItem().toString(),
                            spEmail.getSelectedItem().toString(), point, edNumberBankCard.getText().toString());
                    refCreditCard.add(card);
                }

                HistoryCreditCard historyCreditCard = new HistoryCreditCard(refHistoryCard.getId(),
                        spUID.getSelectedItem().toString(), "Đã nạp thành công ",
                        Double.parseDouble(edNumberAddPoint.getText().toString()), new Timestamp(new Date()));

                Notification notification = new Notification(null, spUID.getSelectedItem().toString(), historyCreditCard
                        .getDescription() + historyCreditCard.getPoint(), 3, new Timestamp(new Date()));

                refNotification.add(notification);

                refHistoryCard.add(historyCreditCard)
                        .addOnSuccessListener(documentReference ->
                                Toast.makeText(getActivity(), "Nạp tiền thành công", Toast.LENGTH_SHORT)
                                        .show());
                ClearField();
            });
        });


        return view;
    }

    private void ClearField() {
        edNumberBankCard.setText("");
        edNumberAddPoint.setText("");
    }
}