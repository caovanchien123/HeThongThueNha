package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.Account;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccountRecyclerView extends RecyclerView.Adapter<AccountRecyclerView.MyViewHolder> {

    private Context context;
    private List<Person> persons;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AccountRecyclerView(Context context, List<Person> persons) {
        this.context = context;
        this.persons = persons;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAccount;
        private TextView tvName, tvEmail;
        private Spinner spLock;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAccount = itemView.findViewById(R.id.img_custom_account);
            tvName = itemView.findViewById(R.id.tv_custom_account_name);
            tvEmail = itemView.findViewById(R.id.tv_custom_account_email);
            spLock = itemView.findViewById(R.id.sp_type_account);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_account_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Person person = persons.get(position);
        holder.tvName.setText(person.getFullName());
        holder.tvEmail.setText(person.getEmail());
        if (!person.getUrl().equals(""))
            Picasso.with(context)
                    .load(person.getUrl())
                    .placeholder(R.drawable.border_person_room)
                    .into(holder.imgAccount);
        else
            holder.imgAccount.setImageResource(R.drawable.ic_baseline_person_24);

        if (person.isLocked())
            holder.spLock.setSelection(1);
        else
            holder.spLock.setSelection(0);

        holder.spLock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.collection("User").whereEqualTo("uid", person.getUid())
                        .addSnapshotListener((value, error) -> {
                            int item = holder.spLock.getSelectedItemPosition();
                            if (error == null) {
                                for (QueryDocumentSnapshot v : value) {
                                    Person person1 = v.toObject(Person.class);
                                    person1.setLocked(item != 0);

                                    db.collection("User").document(v.getId())
                                            .set(person1).addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Thiết lập thành công", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

}
