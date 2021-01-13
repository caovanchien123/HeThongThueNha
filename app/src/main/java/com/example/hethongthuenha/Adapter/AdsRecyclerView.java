package com.example.hethongthuenha.Adapter;

import android.app.AlertDialog;
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
import com.example.hethongthuenha.Model.Ads;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.CreditCard;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class AdsRecyclerView extends RecyclerView.Adapter<AdsRecyclerView.MyViewHolder> {

    private Context context;
    private List<Ads> adsList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public AdsRecyclerView(Context context, List<Ads> adsList) {
        this.context = context;
        this.adsList = adsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRoom, imgRemove;
        TextView tvTitle, tvPrice, tvCountDown;
        CardView cvAds;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRoom = itemView.findViewById(R.id.img_room_ads);
            imgRemove = itemView.findViewById(R.id.img_remove_ads);
            tvTitle = itemView.findViewById(R.id.tv_title_ads);
            tvPrice = itemView.findViewById(R.id.tv_price_ads);
            tvCountDown = itemView.findViewById(R.id.tv_timestamp_ads);
            cvAds = itemView.findViewById(R.id.cv_custom_ads);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_ads_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ads ads = adsList.get(position);

        db.collection("Room").whereEqualTo("room_id", ads.getId_room())
                .get().addOnSuccessListener(v -> {
            if (!v.isEmpty()) {
                for (QueryDocumentSnapshot value : v) {
                    Room room = value.toObject(Room.class);

                    Picasso.with(context).load(room.getStage3().getImagesURL().get(0))
                            .placeholder(R.drawable.home).into(holder.imgRoom);
                    holder.tvTitle.setText(room.getStage1().getTitle());
                    holder.cvAds.setOnClickListener(c -> {
                        Intent intent = new Intent(context, ActivityRoomDetail.class);
                        intent.putExtra("room", room);
                        context.startActivity(intent);
                    });
                }
            }
        });


        holder.tvPrice.setText("" + formatter.format(ads.getPrice()));

        String timeFuture = (String) DateUtils.getRelativeTimeSpanString(ads.getCount_down()
                .getSeconds() * 1000);
        holder.tvCountDown.setText(timeFuture);

        holder.imgRemove.setOnClickListener(v -> {
            NotificationChooseDelete(ads);
        });
        holder.cvAds.setOnClickListener(v -> Toast.makeText(context, "Phòng đã bị xóa hoặc bị lỗi", Toast.LENGTH_SHORT).show());
    }

    private AlertDialog NotificationChooseDelete(Ads ads) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo xóa");
        builder.setMessage("Bạn có thật sự muốn xóa ?");

        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("Có", (dialog, which) -> {
            db.collection("Ads").whereEqualTo("id", ads.getId())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                        db.collection("Ads").document(value.getId())
                                .delete().addOnSuccessListener(v -> {
                            db.collection("Room").whereEqualTo("room_id", ads.getId_room())
                                    .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot value1 : task.getResult()) {
                                        Room room = value1.toObject(Room.class);
                                        room.setOrder(1);

                                        db.collection("Room").document(value1.getId())
                                                .set(room).addOnSuccessListener(aVoid ->
                                                Toast.makeText(context, "Xóa thành phòng !", Toast.LENGTH_SHORT).show());
                                    }
                                }
                            });
                        });
                    }
                }
            });
        });

        AlertDialog show = builder.show();

        return show;
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

}
