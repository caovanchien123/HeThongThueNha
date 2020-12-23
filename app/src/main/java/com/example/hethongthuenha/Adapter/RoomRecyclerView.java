package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.ActivityRoomDetail;
import com.example.hethongthuenha.ActivitySettingPerson;
import com.example.hethongthuenha.CreateRoom.CreateRoomActivity;
import com.example.hethongthuenha.Model.Ads;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.CreditCard;
import com.example.hethongthuenha.Model.Description_Room;
import com.example.hethongthuenha.Model.Image_Room;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RoomRecyclerView extends RecyclerView.Adapter<RoomRecyclerView.MyViewHolder> {


    private List<Room> rooms;
    private Context context;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RoomRecyclerView(Context context, List<Room> rooms) {
        this.rooms = rooms;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRoom, imgMore;
        private TextView tvTitle, tvAddress, tvPrice, tvTypeRoom, tvTimeAdded;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvRoomMain);
            imgRoom = itemView.findViewById(R.id.custom_img_main);
            tvTitle = itemView.findViewById(R.id.tv_title_main);
            imgMore = itemView.findViewById(R.id.custom_more_main);
            tvAddress = itemView.findViewById(R.id.tv_address_main);
            tvPrice = itemView.findViewById(R.id.tv_price_main);
            tvTypeRoom = itemView.findViewById(R.id.tv_type_room_main);
            tvTimeAdded = itemView.findViewById(R.id.tv_time_added_room);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_room_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Room room = rooms.get(position);
        Description_Room stage1 = room.getStage1();
        Image_Room stage3 = room.getStage3();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        String title = stage1.getTitle();
        String address = stage1.getAddress();
        String price = formatter.format(stage1.getPrice());
        String type_room = stage1.getType_room();
        String urlImg = stage3.getImagesURL().get(0);

        holder.tvTitle.setText(title);
        holder.tvAddress.setText(address);
        holder.tvPrice.setText(price + "/Tháng" );
        holder.tvTypeRoom.setText(type_room);


        if(room.getOrder()==2){
            holder.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.vip,0,0,0);
        }else{
            holder.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        }

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(room.getTimeAdded()
                .getSeconds() * 1000);

        holder.tvTimeAdded.setText(timeAgo);

        if (!room.getPerson_id().equals(PersonAPI.getInstance().getUid()))
            holder.imgMore.setVisibility(View.GONE);
        else
            holder.imgMore.setVisibility(View.VISIBLE);

        holder.imgMore.setOnClickListener(v -> {
            NotificationMore(room);
        });

        Picasso.with(context).load(urlImg).error(R.drawable.ic_baseline_error_24)
                .placeholder(R.drawable.loading).into(holder.imgRoom);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityRoomDetail.class);
            intent.putExtra("room", room);
            context.startActivity(intent);
        });
    }

    private void NotificationMore(Room room) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Tùy chọn");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("Chỉnh sửa");
        arrayAdapter.add("Xóa");
        arrayAdapter.add("Quảng cáo");
        arrayAdapter.add("Xem danh sách người thuê");

        builderSingle.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            //String strName = arrayAdapter.getItem(which);
            switch (which) {
                case 0:
                    Intent intent = new Intent(context, CreateRoomActivity.class);
                    intent.putExtra("update", room.getRoom_id());
                    intent.putExtra("room", room);
                    intent.putExtra("roomAdded", room.getTimeAdded());
                    context.startActivity(intent);
                    break;
                case 1:
                    db.collection("Room").whereEqualTo("room_id", room.getRoom_id())
                            .get().addOnCompleteListener(v -> {
                        if (v.isSuccessful()) {
                            for (QueryDocumentSnapshot value : v.getResult()) {
                                db.collection("Room").document(value.getId())
                                        .delete().addOnCompleteListener(c -> {
                                    if (c.isSuccessful())
                                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    });
                    break;
                case 2:
                    ShowDialogRequirement(room);
                    break;
                case 3:
                    ShowDialogBookRoom(room, context);
                    break;
            }
        });
        builderSingle.show();
    }

    public void ShowDialogRequirement(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_ads, null);
        builder.setView(viewLayout);

        Spinner spinner = viewLayout.findViewById(R.id.sp_layout_ads);
        Button buttonRegister = viewLayout.findViewById(R.id.btn_register_ads);
        Button buttonCancel = viewLayout.findViewById(R.id.btn_cancel_ads);


        final AlertDialog show = builder.show();

        buttonRegister.setOnClickListener(v -> {
            String[] typeAds = spinner.getSelectedItem().toString().split("VNĐ/");
            double price = Double.parseDouble(typeAds[0]);
            DocumentReference ref = db.collection("Ads").document();

            try {
                if (price <= PersonAPI.getInstance().getPoint()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());


                    if (price == 60000)
                        cal.add(Calendar.DATE, 7);
                    else if (price == 160000)
                        cal.add(Calendar.DATE, 17);
                    else
                        cal.add(Calendar.DATE, 27);
                    Date date = cal.getTime();
                    Timestamp timestamp = new Timestamp(date);


                    db.collection("Room").whereEqualTo("room_id", room.getRoom_id()).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                                        Room room1 = value.toObject(Room.class);
                                        if (room1.getOrder() == 2) {
                                            Toast.makeText(context, "Đã quảng cáo rồi !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Ads ads = new Ads(ref.getId(), room.getRoom_id(), price, timestamp);
                                            db.collection("Ads").add(ads);
                                            db.collection("CreditCard").whereEqualTo("email_person", PersonAPI.getInstance().getEmail())
                                                    .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                if (!queryDocumentSnapshots1.isEmpty()) {
                                                    for (QueryDocumentSnapshot value1 : queryDocumentSnapshots1) {
                                                        CreditCard card = value1.toObject(CreditCard.class);
                                                        card.setPoint(card.getPoint() - ads.getPrice());
                                                        PersonAPI.getInstance().setPoint(card.getPoint());
                                                        db.collection("CreditCard").document(value1.getId())
                                                                .set(card);
                                                    }
                                                }
                                            });

                                            room1.setOrder(2);
                                            db.collection("Room").document(value.getId())
                                                    .set(room1);
                                            Toast.makeText(context, "Quảng cáo thành công", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });


                } else {
                    ActivitySettingPerson.AddPoint("Bạn không đủ tiền", context);
                }
            } catch (Exception ex) {
                Toast.makeText(context, "Lỗi !", Toast.LENGTH_SHORT).show();
            } finally {
                show.dismiss();
            }

        });

        buttonCancel.setOnClickListener(v -> {
            show.dismiss();
        });
    }

    public static void ShowDialogBookRoom(Room room, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_book_room, null);
        builder.setView(viewLayout);

        List<BookRoom> bookRoomsCare = new ArrayList<>();
        List<BookRoom> bookRoomsConfirm = new ArrayList<>();
        RecyclerView recyclerView = viewLayout.findViewById(R.id.bookRoomrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        BookRoomRecyclerView adapterCare = new BookRoomRecyclerView(context, bookRoomsCare);
        BookRoomRecyclerView adapterConfirm = new BookRoomRecyclerView(context, bookRoomsConfirm);
        Button btnCare, btnConfirm;
        btnCare = viewLayout.findViewById(R.id.btn_list_care_book_room);
        btnConfirm = viewLayout.findViewById(R.id.btn_list_confirm_book_room);


        BookRoomCare(adapterCare, bookRoomsCare, room);
        recyclerView.setAdapter(adapterCare);

        btnCare.setOnClickListener(v -> {
            btnConfirm.setTextColor(Color.GRAY);
            btnCare.setTextColor(Color.WHITE);
            if (bookRoomsCare.size() == 0) {
                BookRoomCare(adapterCare, bookRoomsCare, room);
                recyclerView.setAdapter(adapterCare);
            } else {
                recyclerView.setAdapter(adapterCare);
            }

        });

        btnConfirm.setOnClickListener(v -> {
            btnConfirm.setTextColor(Color.WHITE);
            btnCare.setTextColor(Color.GRAY);
            if (bookRoomsConfirm.size() == 0) {
                BookRoomConfirm(adapterConfirm, bookRoomsConfirm, room);
                recyclerView.setAdapter(adapterConfirm);
            } else
                recyclerView.setAdapter(adapterConfirm);

        });


        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private static void BookRoomCare(BookRoomRecyclerView adapter, List<BookRoom> bookRooms, Room room) {
        db.collection("BookRoom").whereEqualTo("id_room", room.getRoom_id())
                .whereEqualTo("confirm", false).addSnapshotListener((v, e) -> {
            bookRooms.clear();
            if (e == null) {
                for (QueryDocumentSnapshot value : v) {
                    BookRoom bookRoom = value.toObject(BookRoom.class);
                    bookRooms.add(bookRoom);
                }
                Collections.sort(bookRooms, (o1, o2) -> (int) (o2.getBookRoomAdded().getSeconds() - o1.getBookRoomAdded().getSeconds()));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private static void BookRoomConfirm(BookRoomRecyclerView adapter, List<BookRoom> bookRooms, Room room) {
        db.collection("BookRoom").whereEqualTo("id_room", room.getRoom_id())
                .whereEqualTo("confirm", true).addSnapshotListener((v, e) -> {
            bookRooms.clear();
            if (e == null) {
                for (QueryDocumentSnapshot value : v) {
                    BookRoom bookRoom = value.toObject(BookRoom.class);
                    bookRooms.add(bookRoom);
                }
                Collections.sort(bookRooms, (o1, o2) -> (int) (o2.getBookRoomAdded().getSeconds() - o1.getBookRoomAdded().getSeconds()));
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
