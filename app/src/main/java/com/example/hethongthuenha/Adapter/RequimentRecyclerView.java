package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Requirement;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class RequimentRecyclerView extends RecyclerView.Adapter<RequimentRecyclerView.MyViewHolder> {

    private Context context;
    private List<Requirement> requirements;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ListenerRequirement listenerEdit;
    public ListenerRequirement listenerCardView;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public interface ListenerRequirement {
        public void getRequirement(Requirement requirement);
    }

    public void setListenerCardView(ListenerRequirement listenerCardView) {
        this.listenerCardView = listenerCardView;
    }

    public void setListenerEdit(ListenerRequirement listenerEdit) {
        this.listenerEdit = listenerEdit;
    }

    public RequimentRecyclerView(Context context, List<Requirement> requirements) {
        this.context = context;
        this.requirements = requirements;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPerson, imgEdit;
        private TextView tvName, tvAddress, tvPrice, tvTypeRoom;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPerson = itemView.findViewById(R.id.img_custom_person_requiment);
            tvName = itemView.findViewById(R.id.tv_custom_name_requiment);
            tvAddress = itemView.findViewById(R.id.tv_custom_address_requiment);
            tvPrice = itemView.findViewById(R.id.tv_custom_price_requiment);
            tvTypeRoom = itemView.findViewById(R.id.tv_custom_type_room_requiment);
            imgEdit = itemView.findViewById(R.id.img_custom_edit_requirement);
            cardView = itemView.findViewById(R.id.cv_custom_requirement);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_requiment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Requirement requirement = requirements.get(position);

        db.collection("User").whereEqualTo("uid", requirement.getId_person())
                .get().addOnSuccessListener(v -> {
            for (QueryDocumentSnapshot persons : v) {
                Person person = persons.toObject(Person.class);
                if (person.getUrl().equals(""))
                    holder.imgPerson.setImageResource(R.drawable.ic_baseline_person_24);
                else
                    Picasso.with(context).load(person.getUrl())
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(holder.imgPerson);

                holder.tvName.setText(person.getFullName());
            }
        });

        holder.tvAddress.setText(requirement.getAddress());
        holder.tvTypeRoom.setText(requirement.getType_room());
        holder.tvPrice.setText("Giá:" + formatter.format(requirement.getPrice()) + "/Tháng");

        if (!requirement.getId_person().equals(PersonAPI.getInstance().getUid()))
            holder.imgEdit.setVisibility(View.GONE);
        else
            holder.imgEdit.setVisibility(View.VISIBLE);

        holder.imgEdit.setOnClickListener(v -> {
            listenerEdit.getRequirement(requirement);
        });

        holder.cardView.setOnClickListener(v -> {
            listenerCardView.getRequirement(requirement);
        });

    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }

}
