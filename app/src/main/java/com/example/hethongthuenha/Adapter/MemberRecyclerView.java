package com.example.hethongthuenha.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemberRecyclerView extends RecyclerView.Adapter<MemberRecyclerView.MyViewHolder> {

    private Context context;
    private List<Person> persons;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MemberRecyclerView(Context context, List<Person> persons) {
        this.context = context;
        this.persons = persons;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAccount;
        private TextView tvName, tvEmail, tvState;
        private ImageButton imgStateAccount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAccount = itemView.findViewById(R.id.img_custom_account);
            tvName = itemView.findViewById(R.id.tv_custom_account_name);
            tvEmail = itemView.findViewById(R.id.tv_custom_account_email);
            tvState = itemView.findViewById(R.id.tv_custom_account_state);
            imgStateAccount = itemView.findViewById(R.id.img_custom_setting_state);
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
                    .error(R.drawable.person_image_infomation)
                    .placeholder(R.drawable.person_image_infomation)
                    .into(holder.imgAccount);
        else
            holder.imgAccount.setImageResource(R.drawable.ic_baseline_person_24);

        holder.tvState.setText(person.isLocked() ? "Đã khóa" : "Không khóa");
        holder.imgStateAccount.setOnClickListener(v -> {
            showSettingStateAccount(person);
        });
    }

    private void setAccountToFirebase(Person person, boolean lock) {

        db.collection("User").whereEqualTo("uid", person.getUid())
                .get().addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot v : task.getResult()) {
                        Person person1 = v.toObject(Person.class);
                        person1.setLocked(lock);

                        if(task.isComplete()){
                            db.collection("User").document(v.getId())
                                    .set(person1).addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Thiết lập thành công", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                });
    }

    private void showSettingStateAccount(Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_select_account, null);
        builder.setView(viewLayout);

        TextView tvEmail = viewLayout.findViewById(R.id.layout_account_email);
        Button btnLock = viewLayout.findViewById(R.id.layout_btn_lock);
        Button btnUnlock = viewLayout.findViewById(R.id.layout_btn_unlock);

        final AlertDialog show = builder.show();

        tvEmail.setText(person.getEmail());
        if (person.isLocked()) {
            btnLock.setBackgroundColor(R.drawable.border_selected_account);
        } else {
            btnUnlock.setBackgroundColor(R.drawable.border_selected_account);
        }

        btnUnlock.setOnClickListener(v -> {
            setAccountToFirebase(person, false);
            show.dismiss();
        });
        btnLock.setOnClickListener(v -> {
            setAccountToFirebase(person, true);
            show.dismiss();
        });

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

}
