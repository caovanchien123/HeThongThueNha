package com.example.hethongthuenha.PersonSetting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.R;

import java.util.ArrayList;

public class CustomAdapterPersonInformation extends RecyclerView.Adapter<CustomAdapterPersonInformation.ViewHolder> {

    Context context;
    ArrayList<String> rooms;

    public CustomAdapterPersonInformation(Context context, ArrayList<String> rooms) {
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

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvNumber, tvDiaChi;
        ImageView imgItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.person_name_home);
            tvPrice = itemView.findViewById(R.id.person_price_home);
            tvNumber = itemView.findViewById(R.id.person_number_person);
            tvDiaChi = itemView.findViewById(R.id.person_diachi);
            imgItem = itemView.findViewById(R.id.person_image_item);
        }
    }
}
