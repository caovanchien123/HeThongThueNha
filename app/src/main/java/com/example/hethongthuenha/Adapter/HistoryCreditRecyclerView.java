package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.HistoryCreditCard;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class HistoryCreditRecyclerView extends RecyclerView.Adapter<HistoryCreditRecyclerView.MyViewHolder> {

    private Context context;
    private List<HistoryCreditCard> historyCreditCards;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public HistoryCreditRecyclerView(Context context, List<HistoryCreditCard> historyCreditCards) {
        this.context = context;
        this.historyCreditCards = historyCreditCards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_history_credit, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryCreditCard hCard = historyCreditCards.get(position);

        db.collection("User").whereEqualTo("uid", hCard.getId_person())
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

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(hCard.getCreditCardAdded()
                .getSeconds() * 1000);
        holder.tvDescription.setText(hCard.getDescription() + formatter.format(hCard.getPoint()));
        holder.tvAdded.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return historyCreditCards.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName, tvDescription, tvAdded;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_custom_history_credit);
            tvName = itemView.findViewById(R.id.tv_custom_name_hCredit);
            tvDescription = itemView.findViewById(R.id.tv_custom_description_hCredit);
            tvAdded = itemView.findViewById(R.id.tv_custom_added_hCredit);
        }
    }
}
