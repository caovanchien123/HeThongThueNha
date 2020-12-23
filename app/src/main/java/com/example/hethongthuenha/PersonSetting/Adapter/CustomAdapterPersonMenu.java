package com.example.hethongthuenha.PersonSetting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.PersonItemMenu;
import com.example.hethongthuenha.R;

import java.util.ArrayList;

public class CustomAdapterPersonMenu extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PersonItemMenu> personItemMenus;
    ItemClickListener itemClickListener;
    private static final int RONG = 0;
    private static final int ITEM = 1;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomAdapterPersonMenu(Context context, ArrayList<PersonItemMenu> personItemMenus) {
        this.context = context;
        this.personItemMenus = personItemMenus;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM ){
            View view = LayoutInflater.from(context).inflate(R.layout.custom_item_person_setting, parent, false);
            return new ViewHolder(view);
        }else if(viewType == RONG){
            View view = LayoutInflater.from(context).inflate(R.layout.person_bank_item, parent, false);
            return new ViewHolderRong(view);
        }
        else
            throw new RuntimeException("Could not inflate layout");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            PersonItemMenu itemRecycleView = personItemMenus.get(position);
            ((ViewHolder) holder).name.setText(itemRecycleView.getName());
            ((ViewHolder) holder).imageItem.setImageResource(itemRecycleView.getImageItem());
            ((ViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    itemClickListener.onClick(view, position, isLongClick);
                }
            });
        }else if(holder instanceof ViewHolderRong){

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1 || position == 0)
            return RONG;
        else
            return ITEM;
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

    class ViewHolderRong extends RecyclerView.ViewHolder{
        public ViewHolderRong(@NonNull View itemView) {
            super(itemView);

        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
}


