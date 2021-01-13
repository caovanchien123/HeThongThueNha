package com.example.hethongthuenha.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.HistoryCreditCard;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Refund;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class RefundRecyclerView extends RecyclerView.Adapter<RefundRecyclerView.MyViewHolder> {

    private Context context;
    private List<Refund> refunds;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference refHistoryCard = db.collection("History-CreditCard");
    private CollectionReference refNotication = db.collection("Notification");
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private ProgressDialog progressDialog;

    public RefundRecyclerView(Context context, List<Refund> refunds) {
        this.context = context;
        this.refunds = refunds;
        this.progressDialog = new ProgressDialog(context);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imgDone;
        TextView tvName, tvPrice, tvBankCard, tvTimestamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_custom_person_refund);
            imgDone = itemView.findViewById(R.id.img_done_refund);
            tvName = itemView.findViewById(R.id.tv_custom_name_refund);
            tvPrice = itemView.findViewById(R.id.tv_custom_price_refund);
            tvBankCard = itemView.findViewById(R.id.tv_bank_card_refund);
            tvTimestamp = itemView.findViewById(R.id.tv_custom_timestamp_refund);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_refund, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Refund refund = refunds.get(position);

        db.collection("User").whereEqualTo("uid", refund.getId_person())
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                    Person person = value.toObject(Person.class);
                    if (!person.getUrl().equals(""))
                        Picasso.with(context).load(person.getUrl())
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(holder.imgAvatar);

                    holder.tvName.setText(person.getFullName());
                }
            }
        });

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(refund.getRefundAdded()
                .getSeconds() * 1000);

        holder.tvTimestamp.setText(timeAgo);
        holder.tvBankCard.setText(refund.getBankCard());
        holder.tvPrice.setText("" + formatter.format(refund.getPrice()));

        holder.imgDone.setOnClickListener(v -> {
            progressDialog.setMessage("Làm ơn vui lỏng chờ");
            progressDialog.show();
            db.collection("Refund").whereEqualTo("id", refund.getId())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot value : queryDocumentSnapshots) {

                        db.collection("Refund").document(value.getId())
                                .delete().addOnSuccessListener(aVoid -> {
                            HistoryCreditCard historyCreditCard = new HistoryCreditCard(refHistoryCard.getId(),
                                    refund.getId_person(), "Đã rút tiền ",
                                    refund.getPrice(), new Timestamp(new Date()));

                            Notification notification = new Notification(null, refund.getId_person(), historyCreditCard
                                    .getDescription() + historyCreditCard.getPoint(), 3, new Timestamp(new Date()));

                            refNotication.add(notification).addOnSuccessListener(documentReference ->
                                    refHistoryCard.add(historyCreditCard).
                                            addOnSuccessListener(documentReference1 -> {
                                                Toast.makeText(context, "Hoàn tiền thành công", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }));


                        });
                    }
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return refunds.size();
    }

}
