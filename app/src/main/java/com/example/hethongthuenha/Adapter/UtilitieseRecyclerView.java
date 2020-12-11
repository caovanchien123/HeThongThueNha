package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.Model.Utilities_Room;
import com.example.hethongthuenha.R;

import java.util.List;

public class UtilitieseRecyclerView extends RecyclerView.Adapter<UtilitieseRecyclerView.MyViewHolder> {
    private Utilities_Room utilities;
    private Context context;

    public UtilitieseRecyclerView(Context context,Utilities_Room utilities) {
        this.utilities = utilities;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.custom_utilities_detail,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String description=utilities.getDescription_utility().get(position);

        holder.descriptionUtilities.setText(description);

        if(description.equals("Wifi"))
            holder.imageUtilities.setImageResource(R.drawable.wifi);
        else if(description.equals("Hồ bơi"))
            holder.imageUtilities.setImageResource(R.drawable.pool);
        else if(description.equals("Máy lạnh"))
            holder.imageUtilities.setImageResource(R.drawable.airconditioner);
        else if(description.equals("Tủ lạnh"))
            holder.imageUtilities.setImageResource(R.drawable.fridge);
        else
            holder.imageUtilities.setImageResource(R.drawable.verified);
    }

    @Override
    public int getItemCount() {
        //neu tien ich it hon san pham gioi han
        return utilities.getDescription_utility().size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageUtilities;
        private TextView descriptionUtilities;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUtilities=itemView.findViewById(R.id.custom_utilities_img);
            descriptionUtilities=itemView.findViewById(R.id.custom_utilities_description);
        }
    }
}
