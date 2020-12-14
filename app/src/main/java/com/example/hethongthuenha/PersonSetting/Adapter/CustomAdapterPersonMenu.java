package com.example.hethongthuenha.PersonSetting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.PersonItemMenu;
import com.example.hethongthuenha.R;

import java.util.ArrayList;

public class CustomAdapterPersonMenu extends RecyclerView.Adapter<CustomAdapterPersonMenu.ViewHolder> {
    Context context;
    ArrayList<PersonItemMenu> personItemMenus;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomAdapterPersonMenu(Context context, ArrayList<PersonItemMenu> personItemMenus) {
        this.context = context;
        this.personItemMenus = personItemMenus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_person_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonItemMenu itemRecycleView = personItemMenus.get(position);
        holder.name.setText(itemRecycleView.getName());
        holder.imageItem.setImageResource(itemRecycleView.getImageItem());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                itemClickListener.onClick(view, position, isLongClick);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personItemMenus.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageItem;
        TextView name;
        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.person_image_item);
            name = itemView.findViewById(R.id.person_name_item);
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

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
}


