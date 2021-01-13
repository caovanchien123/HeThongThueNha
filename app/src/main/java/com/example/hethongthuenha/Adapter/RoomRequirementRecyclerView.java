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

import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class RoomRequirementRecyclerView extends RecyclerView.Adapter<RoomRequirementRecyclerView.MyViewHolder> {

    private Context context;
    private List<Room> rooms;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public ListenerRoom listener;
    public interface ListenerRoom{
        public void onClick(Room room);
    }

    public RoomRequirementRecyclerView(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    public ListenerRoom getListener() {
        return listener;
    }

    public void setListener(ListenerRoom listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_select_room_requirement, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.tvTitle.setText(room.getStage1().getTitle());
        holder.tvAddress.setText(room.getStage1().getAddress());
        holder.tvPrice.setText(formatter.format(room.getStage1().getPrice()));
        Picasso.with(context).load(room.getStage3().getImagesURL().get(0))
                .error(R.drawable.ic_baseline_error_24)
                .placeholder(R.drawable.loading)
                .into(holder.imgRoom);

        holder.cardView.setOnClickListener(v->{
            listener.onClick(room);
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvTitle, tvAddress, tvPrice;
        private ImageView imgRoom;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_room_requirement);
            tvTitle = itemView.findViewById(R.id.tv_title_room_requirement);
            tvAddress = itemView.findViewById(R.id.tv_address_room_requirement);
            tvPrice = itemView.findViewById(R.id.tv_price_room_requirement);
            imgRoom = itemView.findViewById(R.id.img_custom_room_requiment);
        }
    }
}
