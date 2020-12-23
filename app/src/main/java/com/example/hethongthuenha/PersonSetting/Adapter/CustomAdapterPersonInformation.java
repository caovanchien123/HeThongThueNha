package com.example.hethongthuenha.PersonSetting.Adapter;

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

import com.example.hethongthuenha.ActivityRoomDetail;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.PersonSetting.Dialog.PersonDialogMenuItem;
import com.example.hethongthuenha.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterPersonInformation extends RecyclerView.Adapter<CustomAdapterPersonInformation.ViewHolder> {
    Context context;
    ArrayList<Room> rooms;

    public CustomAdapterPersonInformation(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_recycleview_home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.tvNgay.setText((String) DateUtils.getRelativeTimeSpanString(room.getTimeAdded().getSeconds() * 1000));
        holder.tvDiaChi.setText(room.getStage1().getAddress());
        holder.tvName.setText(room.getStage1().getTitle());
        holder.tvPrice.setText(room.getStage1().getPrice()+"");
        holder.tvKieu.setText(room.getStage1().getType_room());
        Picasso.with(context).load(room.getStage3().getImagesURL().get(0)).into(holder.imgCustom);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                    PersonDialogMenuItem menuItem = new PersonDialogMenuItem(context, room);
                    menuItem.setRoomDataChanger(new PersonDialogMenuItem.RoomDataChanger() {
                        @Override
                        public void removeRoom() {
                            rooms.remove(room);
                            notifyDataSetChanged();
                        }
                    });
                }else {
                    Intent intent = new Intent(context, ActivityRoomDetail.class);
                    intent.putExtra("room", room);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName, tvPrice, tvNgay, tvDiaChi, tvKieu;
        ImageView imgCustom;
        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_title_main);
            tvKieu = itemView.findViewById(R.id.tv_type_room_main);
            tvPrice = itemView.findViewById(R.id.tv_price_main);
            tvNgay = itemView.findViewById(R.id.tv_time_added_room);
            tvDiaChi = itemView.findViewById(R.id.tv_address_main);
            imgCustom = itemView.findViewById(R.id.custom_img_main);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    public interface ItemClickListener{
        void onClick(View view, int position, boolean isLongClick);
    }
}
