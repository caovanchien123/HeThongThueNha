package com.example.hethongthuenha.Adapter;

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

import com.example.hethongthuenha.ActivityChat;
import com.example.hethongthuenha.ActivityPerson;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotficationRecyclerView extends RecyclerView.Adapter<NotficationRecyclerView.MyViewHolder> {

    private Context context;
    private List<Notification> notifications;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public NotficationRecyclerView(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPerson;
        private TextView tvName, tvDescription, tvAdded;
        private CardView cvNotfication;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPerson = itemView.findViewById(R.id.img_custom_person_notification);
            tvName = itemView.findViewById(R.id.tv_custom_name_notification);
            tvDescription = itemView.findViewById(R.id.tv_custom_description_notification);
            tvAdded = itemView.findViewById(R.id.tv_custom_added_notification);
            cvNotfication = itemView.findViewById(R.id.cv_custom_notitification);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        db.collection("User").whereEqualTo("uid", notification.getId_person_provide())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    Person person = value.toObject(Person.class);
                    if (!person.getUrl().equals(""))
                        Picasso.with(context).load(person.getUrl())
                                .placeholder(R.drawable.ic_baseline_person_24).into(holder.imgPerson);

                    holder.tvName.setText(person.getFullName());

                    holder.cvNotfication.setOnClickListener(v -> {

                        if (notification.getType_notification() == Notification.CHAT) {
                            Intent intent = new Intent(context, ActivityChat.class);
                            intent.putExtra("toEmail", person.getEmail());
                            intent.putExtra("toName", person.getFullName());
                            context.startActivity(intent);
                        } else if(notification.getType_notification() == Notification.ROOM) {
                            Intent intent = new Intent(context, ActivityPerson.class);
                            intent.putExtra("id_person", notification.getId_person_provide());
                            context.startActivity(intent);
                        }
                    });
                }
            }
        });
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(notification.getNotificationAdded()
                .getSeconds() * 1000);
        holder.tvDescription.setText(notification.getDescription());
        holder.tvAdded.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


}
