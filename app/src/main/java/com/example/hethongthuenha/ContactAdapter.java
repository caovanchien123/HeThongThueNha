package com.example.hethongthuenha;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Adapter.PhongTro;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    ArrayList<PhongTro> listData;
    Context context;

    public ContactAdapter(Context context, ArrayList<PhongTro> list) {
        this.listData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_contact_list_layout, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PhongTro dataObj = listData.get(position);
        holder.nameTXT.setText(dataObj.getRoomName());
        holder.priceTXT.setText(dataObj.getRoomPrice());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTXT, priceTXT;
        ImageView imgWarning;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTXT = itemView.findViewById(R.id.Name);
            priceTXT = itemView.findViewById(R.id.Price);
            imgWarning = itemView.findViewById(R.id.warning);

        }
    }
}




