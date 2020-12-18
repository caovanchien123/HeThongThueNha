package com.example.hethongthuenha.PersonSetting.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.ModelA.Room;
import com.example.hethongthuenha.PersonSetting.Dialog.PersonDialogMenuItem;
import com.example.hethongthuenha.PersonSetting.PersonInformationActivity;
import com.example.hethongthuenha.R;

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
        holder.tvNgay.setText(room.getStage1().getType_date()+"");
        holder.tvDiaChi.setText(room.getStage1().getAddress());
        holder.tvName.setText(room.getStage1().getTitle());
        holder.tvPrice.setText(room.getStage1().getPrice()+"");
        holder.tvKieu.setText(room.getStage1().getType_room());
        holder.imgCustom.setImageResource(R.drawable.person_image_infomation);
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonDialogMenuItem dialogMenuItem = new PersonDialogMenuItem(context, room);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvNgay, tvDiaChi, tvKieu;
        ImageView imgItem, imgCustom;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_title_main);
            tvKieu = itemView.findViewById(R.id.tv_type_room_main);
            tvPrice = itemView.findViewById(R.id.tv_price_main);
            tvNgay = itemView.findViewById(R.id.tv_time_added_room);
            tvDiaChi = itemView.findViewById(R.id.tv_address_main);
            imgItem = itemView.findViewById(R.id.custom_more_main);
            imgCustom = itemView.findViewById(R.id.custom_img_main);
            layout = itemView.findViewById(R.id.cvRoomMain);
        }
    }
}
