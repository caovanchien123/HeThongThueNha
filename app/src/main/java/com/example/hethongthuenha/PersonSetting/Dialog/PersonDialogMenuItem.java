package com.example.hethongthuenha.PersonSetting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hethongthuenha.ModelA.Room;
import com.example.hethongthuenha.PersonSetting.Adapter.CustomAdapterPersonInformation;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersonDialogMenuItem extends Dialog {
    LinearLayout lnXoa, lnSua, lnQC, lnDSNguoiDung;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Context context;
    Room room;
    RoomDataChanger roomDataChanger;

    public void setRoomDataChanger(RoomDataChanger roomDataChanger) {
        this.roomDataChanger = roomDataChanger;
    }

    public PersonDialogMenuItem(@NonNull Context context, Room room) {
        super(context);
        setContentView(R.layout.person_information_dialog_item_menu);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.room = room;
        this.context = context;
        show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lnDSNguoiDung = findViewById(R.id.person_information_NguoiThue);
        lnXoa = findViewById(R.id.person_information_delete);
        lnSua = findViewById(R.id.person_information_edit);
        lnQC = findViewById(R.id.person_information_qc);
        setEven();
    }

    private void setEven() {
        lnDSNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Danh Sach Nguoi dung", Toast.LENGTH_LONG).show();
            }
        });
        DialogProgress progress = new DialogProgress(context,"Đang xóa " + room.getStage1().getTitle());
        lnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Room").whereEqualTo("room_id", room.getRoom_id()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        progress.show();
                        for (QueryDocumentSnapshot snapshot : value){
                            firestore.collection("Room").document(snapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    roomDataChanger.removeRoom();
                                    progress.cancel();
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progress.cancel();
                                    Toast.makeText(context, "Lỗi khi xóa", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                cancel();
            }
        });
        lnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Sua", Toast.LENGTH_LONG).show();
            }
        });
        lnQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "QC", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface RoomDataChanger{
        void removeRoom();
    }
}
