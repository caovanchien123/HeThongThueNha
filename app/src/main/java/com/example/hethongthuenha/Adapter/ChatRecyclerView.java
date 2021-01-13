package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
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

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.ActivityRoomDetail;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.Chat;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;


public class ChatRecyclerView extends RecyclerView.Adapter<ChatRecyclerView.MyViewHolder> {

    private Context context;
    private List<Chat> chats;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public ChatRecyclerView(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChat, tvAddress, tvPrice, tvTypeRoom;
        private LinearLayout linearChat, linearFooter, linearImage;
        private ImageView imgChat, imgAgree;
        private CardView cvChat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChat = itemView.findViewById(R.id.tv_custom_chat);
            linearChat = itemView.findViewById(R.id.linear_custom_chat);
            linearFooter = itemView.findViewById(R.id.linear_footer_custom_chat);
            linearImage = itemView.findViewById(R.id.linearImage);
            imgChat = itemView.findViewById(R.id.img_custom_chat);
            imgAgree = itemView.findViewById(R.id.img_agree_chat);
            tvAddress = itemView.findViewById(R.id.tv_address_chat);
            tvPrice = itemView.findViewById(R.id.tv_price_chat);
            tvTypeRoom = itemView.findViewById(R.id.tv_type_room_chat);
            cvChat = itemView.findViewById(R.id.cv_custom_chat);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_chat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.tvChat.setText(chat.getText());

        String fromUser = PersonAPI.getInstance().getEmail();
        String fromUserChat = chat.getFrom_email_person();


        if (!fromUser.equals(fromUserChat)) {
            holder.linearChat.setGravity(Gravity.START);
            holder.tvChat.setBackgroundResource(R.drawable.border_chat_left);
            holder.tvChat.setTextColor(Color.BLACK);
        } else {
            holder.linearChat.setGravity(Gravity.END);
            holder.tvChat.setBackgroundResource(R.drawable.border_chat_right);
            holder.tvChat.setTextColor(Color.WHITE);
        }


        //neu khong co hinh anh
        if (chat.getUrl().equals("")) {
            holder.imgChat.setVisibility(View.GONE);
            holder.imgAgree.setVisibility(View.GONE);
            holder.tvAddress.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvTypeRoom.setVisibility(View.GONE);
            holder.cvChat.setVisibility(View.GONE);

            holder.tvChat.setVisibility(View.VISIBLE);

        } else if (chat.getText().equals("")) {
            holder.tvChat.setVisibility(View.GONE);
            holder.imgAgree.setVisibility(View.GONE);
            holder.tvAddress.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvTypeRoom.setVisibility(View.GONE);
            holder.linearFooter.setVisibility(View.GONE);
            Picasso.with(context).load(chat.getUrl()).placeholder(R.drawable.loading)
                    .into(holder.imgChat);
            holder.linearImage.setPadding(0, 0, 0, 0);

            holder.imgChat.setVisibility(View.VISIBLE);
            holder.cvChat.setVisibility(View.VISIBLE);
            holder.cvChat.setOnClickListener(null);
        }
        //neu co hinh anh va chu
        else {
            holder.imgChat.setVisibility(View.VISIBLE);
            if (!chat.getFrom_email_person().equals(PersonAPI.getInstance().getEmail())) {
                holder.imgAgree.setVisibility(View.VISIBLE);
            } else {
                holder.imgAgree.setVisibility(View.GONE);
            }
            holder.linearImage.setPadding(20, 20, 20, 20);

            holder.tvAddress.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvTypeRoom.setVisibility(View.VISIBLE);
            holder.cvChat.setVisibility(View.VISIBLE);
            holder.tvChat.setVisibility(View.VISIBLE);
            holder.linearFooter.setVisibility(View.VISIBLE);

            Picasso.with(context).load(chat.getUrl()).placeholder(R.drawable.loading)
                    .into(holder.imgChat);


            db.collection("Room").get()
                    .addOnCompleteListener(v -> {
                        if (v.isSuccessful()) {
                            for (QueryDocumentSnapshot value : v.getResult()) {
                                Room room = value.toObject(Room.class);
                                for (String s : room.getStage3().getImagesURL())
                                    if (s.equals(chat.getUrl())) {
                                        holder.tvAddress.setText("Địa chỉ:" + room.getStage1().getAddress());
                                        holder.tvPrice.setText("Giá:" + formatter.format(room.getStage1().getPrice()));
                                        holder.tvTypeRoom.setText(room.getStage1().getType_room());

                                        //neu day chi la gioi thieu phong
                                        if (chat.getText().equals("Tôi có thể đáp ứng cho bạn " + room.getStage1().getTitle()))
                                            holder.imgAgree.setVisibility(View.GONE);

                                        holder.cvChat.setOnClickListener(z -> {
                                            moveToRoomDetail(room);
                                        });

                                        holder.imgAgree.setOnClickListener(c -> {
                                            handleChooseAgree(room, chat);
                                        });
                                    }
                            }
                        }
                    });


            holder.cvChat.setOnClickListener(z -> {
                Toast.makeText(context, "Phòng đã bị xóa hoặc chưa tải xong ! ", Toast.LENGTH_SHORT).show();
            });

            holder.imgAgree.setOnClickListener(v -> {
                Toast.makeText(context, "Phòng đã bị xóa hoặc bị lỗi", Toast.LENGTH_SHORT).show();
            });
        }


    }

    public void moveToRoomDetail(Room room) {
        Intent intent = new Intent(context, ActivityRoomDetail.class);
        intent.putExtra("room", room);
        context.startActivity(intent);
    }

    public void handleChooseAgree(Room room, Chat chat) {
        DocumentReference ref = db.collection("BookRoom").document();
        Timestamp timestamp = new Timestamp(new Date());
        db.collection("User").whereEqualTo("email", chat.getFrom_email_person())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot users : task.getResult()) {
                    Person person = users.toObject(Person.class);

                    db.collection("BookRoom").whereEqualTo("id_person", person.getUid())
                            .whereEqualTo("id_room", room.getRoom_id())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            BookRoom bookRoom = new BookRoom(ref.getId(),
                                    person.getUid(), room.getRoom_id(), "Trả sau", timestamp, false, 0);

                            db.collection("BookRoom").add(bookRoom);

                            Notification notification = new Notification(room.getPerson_id(),
                                    person.getUid(), "Trả sau thành công tại phòng " + room.getStage1().getTitle(), 2, timestamp);

                            db.collection("Notification").add(notification).addOnCompleteListener(x -> {
                                Toast.makeText(context, "Bạn đã đưa vào danh sách xem sau  ", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            Toast.makeText(context, "Người này đã được đưa vào rồi ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
