package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.ActivityPerson;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.Commission;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class PayRecyclerView extends RecyclerView.Adapter<PayRecyclerView.MyViewHolder> {


    private Context context;
    private List<Commission> commissions;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public PayRecyclerView(Context context, List<Commission> commissions) {
        this.context = context;
        this.commissions = commissions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_pay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Commission commission = commissions.get(position);

        db.collection("User").whereEqualTo("uid", commission.getId_person())
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                    Person person = value.toObject(Person.class);
                    if (!person.getUrl().equals(""))
                        Picasso.with(context).load(person.getUrl())
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(holder.imgAvatar);

                    holder.tvName.setText(person.getFullName());
                    holder.cardView.setOnClickListener(c -> {
                        Intent intent = new Intent(context, ActivityPerson.class);
                        intent.putExtra("id_person", person.getUid());
                        context.startActivity(intent);
                    });
                }
            }

            holder.tvPrice.setText("" + formatter.format(commission.getPrice()));


            db.collection("Commission").document(commission.getId_person())
                    .get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Commission commissionUpdate = documentSnapshot.toObject(Commission.class);

                    if (commissionUpdate.getTotalDay() < commission.getTotalDay()) {
                        commissionUpdate.setTotalDay(commission.getTotalDay());
                        commissionUpdate.setPrice(commission.getPrice());
                    }


                    if (commissionUpdate.getLastPaid() == null) {
                        commissionUpdate.setLastPaid(commission.getLastPaid());
                    }

                    if(commissionUpdate.getTotalDay()==0){
                        holder.tvLastPaid.setVisibility(View.GONE);
                    }
                    db.collection("Commission").document(commission.getId_person())
                            .set(commissionUpdate);

                    String timeAgo = (String) DateUtils.getRelativeTimeSpanString(commissionUpdate.getLastPaid()
                            .getSeconds() * 1000);

                    holder.tvLastPaid.setText("Trả lần cuối:" + timeAgo);
                    holder.tvPrice.setText("" + formatter.format(commissionUpdate.getPrice()));
                    holder.tvTotalDay.setText("Tổng cộng:" + commissionUpdate.getTotalDay() + " ngày");
                } else {
                    //khi chua co !
                    db.collection("Commission").document(commission.getId_person())
                            .set(commission);
                    holder.tvLastPaid.setText("Chưa trả lần nào");
                    holder.tvPrice.setText("" + formatter.format(commission.getPrice()));
                    holder.tvTotalDay.setText("Tổng cộng:" + commission.getTotalDay() + " ngày");
                }
            });


        });

    }

    @Override
    public int getItemCount() {
        return commissions.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName, tvPrice, tvTotalDay, tvLastPaid;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_avatar_pay);
            tvName = itemView.findViewById(R.id.tv_custom_name_pay);
            tvPrice = itemView.findViewById(R.id.tv_price_pay);
            tvTotalDay = itemView.findViewById(R.id.tv_total_day_pay);
            tvLastPaid = itemView.findViewById(R.id.tv_last_paid_pay);
            cardView = itemView.findViewById(R.id.cv_custom_pay);
        }
    }
}
