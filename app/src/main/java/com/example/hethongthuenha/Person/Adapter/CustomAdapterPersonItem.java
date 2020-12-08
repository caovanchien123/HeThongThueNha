package com.example.hethongthuenha.Person.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Person.Model.PersonItemRecycleView;
import com.example.hethongthuenha.R;

import java.util.ArrayList;

public class CustomAdapterPersonItem extends RecyclerView.Adapter<CustomAdapterPersonItem.ViewHolder> {
    Context context;
    ArrayList<PersonItemRecycleView> personItemRecycleViews;

    public CustomAdapterPersonItem(Context context, ArrayList<PersonItemRecycleView> personItemRecycleViews) {
        this.context = context;
        this.personItemRecycleViews = personItemRecycleViews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_person_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Toast.makeText(context, "sss" + position, Toast.LENGTH_SHORT).show();
        PersonItemRecycleView itemRecycleView = personItemRecycleViews.get(position);
        holder.name.setText(itemRecycleView.getName());
        holder.imageItem.setImageResource(itemRecycleView.getImageItem());
    }

    @Override
    public int getItemCount() {
        return personItemRecycleViews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageItem;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.person_image_item);
            name = itemView.findViewById(R.id.person_name_item);
        }
    }
}
