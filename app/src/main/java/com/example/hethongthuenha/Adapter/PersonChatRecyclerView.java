package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.ActivityChat;
import com.example.hethongthuenha.Model.HistoryChat;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonChatRecyclerView extends RecyclerView.Adapter<PersonChatRecyclerView.MyViewHolder> {

    private Context context;
    private List<Map<String,Object>> persons;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public PersonChatRecyclerView(Context context, List<Map<String, Object>> persons) {
        this.context = context;
        this.persons = persons;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imgChat;
        private TextView tvName,tvLastChat,tvTime;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChat=itemView.findViewById(R.id.img_custom_person_chat);
            tvName=itemView.findViewById(R.id.tv_custom_name_chat);
            tvLastChat=itemView.findViewById(R.id.tv_custom_last_chat);
            tvTime=itemView.findViewById(R.id.tv_custom_time_last_chat);
            cardView=itemView.findViewById(R.id.cv_custom_person_chat);
        }
    }

    @NonNull
    @Override
    public PersonChatRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.custom_person_chat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonChatRecyclerView.MyViewHolder holder, int position) {
        Map<String,Object> map=persons.get(position);

        db.collection("User").whereEqualTo("email",map.get("toEmail"))
                .get().addOnSuccessListener(v->{
                for(QueryDocumentSnapshot persons:v){
                    Person person=persons.toObject(Person.class);
                    if(person.getUrl().equals(""))
                        holder.imgChat.setImageResource(R.drawable.ic_baseline_person_24);
                    else
                        Picasso.with(context).load(person.getUrl())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(holder.imgChat);
                }
        });

        holder.tvName.setText(map.get("toName").toString());
        holder.tvLastChat.setText(map.get("lastChat").toString());

        //date
        Date date=((Timestamp)map.get("chatAdded")).toDate();
        DateFormat f = new SimpleDateFormat("hh:mm:ss DD-MM-yyyy");
        holder.tvTime.setText(f.format(date));

        holder.cardView.setOnClickListener(v -> {
            Intent intent=new Intent(context,ActivityChat.class);
            intent.putExtra("toEmail",map.get("toEmail").toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
