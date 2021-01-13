package com.example.hethongthuenha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.Commission;
import com.example.hethongthuenha.Model.CreditCard;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Refund;
import com.example.hethongthuenha.Model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivitySettingPerson extends AppCompatActivity {
    private ListView lvSetting;
    private FirebaseAuth mAuth;
    private TextView tvName, tvPoint, tvEmail;
    private EditText edMoney, edPasswordOld, edPasswordNew, edRepasswordNew;
    private ImageView imgAvatar;
    private Button btnRefund, btnCancel, btnChange;
    private CreditCard card;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("User");
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private Uri imageUri;
    private StorageReference storageReference;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_person);
        mAuth = FirebaseAuth.getInstance();
        lvSetting = findViewById(R.id.lvSettingPerson);
        tvName = findViewById(R.id.tv_name_setting_person);
        tvPoint = findViewById(R.id.tv_point_setting_person);
        tvEmail = findViewById(R.id.tv_email_setting_person);
        imgAvatar = findViewById(R.id.img_avatar_person);
        storageReference = FirebaseStorage.getInstance().getReference();

        db.collection("User").whereEqualTo("uid", PersonAPI.getInstance().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    Person person = value.toObject(Person.class);
                    tvName.setText(person.getFullName());
                    tvEmail.setText(person.getEmail());
                    if (!person.getUrl().equals("")) {
                        Picasso.with(ActivitySettingPerson.this)
                                .load(person.getUrl()).placeholder(R.drawable.ic_baseline_person_24)
                                .into(imgAvatar);
                    }
                }
            }
        });


        db.collection("CreditCard").whereEqualTo("email_person", PersonAPI.getInstance().getEmail())
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        for (QueryDocumentSnapshot v : value) {
                            CreditCard card = v.toObject(CreditCard.class);
                            tvPoint.setText(formatter.format(card.getPoint()));
                            PersonAPI.getInstance().setPoint(card.getPoint());
                        }
                    }
                });


        ArrayList<String> controls = new ArrayList<>();
        controls.add("Xem trang cá nhân");
        controls.add("Thanh toán hoa hồng");
        controls.add("Nạp tiền");
        controls.add("Rút tiền");
        controls.add("Đăng xuất");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, controls);
        lvSetting.setAdapter(adapter);


        lvSetting.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                Intent intent = new Intent(this, ActivityPerson.class);
                intent.putExtra("id_person", PersonAPI.getInstance().getUid());
                startActivity(intent);
            } else if (position == 1) {
                db.collection("Commission").document(PersonAPI.getInstance().getUid())
                        .get().addOnSuccessListener(documentSnapshot -> {
                    Commission commission = documentSnapshot.toObject(Commission.class);
                    if (commission.getPrice() != 0)
                        PayRoom(commission.getPrice());
                    else
                        Toast.makeText(this, "Bạn đã thanh toán rồi !", Toast.LENGTH_SHORT).show();
                });
            } else if (position == 2) {
                AddPoint("Nội dung cú pháp nạp tiền", ActivitySettingPerson.this);
            } else if (position == 3) {
                ShowDialogRefund();
            } else if (position == 4) {
                mAuth.signOut();
            }
        });


        imgAvatar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    PickImageFromGallery();
                }
            } else
                PickImageFromGallery();
        });
    }

    public void PickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void SaveImage() {
        final StorageReference filepath = storageReference.child("Avatar")
                .child(PersonAPI.getInstance().getUid());

        filepath.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();

                    collectionReference.whereEqualTo("uid", PersonAPI.getInstance().getUid())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                                Person person = value.toObject(Person.class);
                                person.setUrl(url);

                                db.collection("User").document(value.getId())
                                        .set(person)
                                        .addOnSuccessListener(v -> {
                                            Toast.makeText(ActivitySettingPerson.this, "Đổi ảnh thành công", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    });
                }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    PickImageFromGallery();
                else
                    Toast.makeText(this, "Permisstion deny", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imgAvatar.setImageURI(data.getData());
            imageUri = data.getData();
            SaveImage();
        }
    }

    public static void AddPoint(String title, Context context) {

        String contact = "0169xxxxxx";
        String text = "Nội dung cú pháp nạp tiền:\n" +
                "\nID:" + PersonAPI.getInstance().getUid() + "\n" +
                "\nEmail:" + PersonAPI.getInstance().getEmail() + "\n" +
                " \n" +
                "-----------------------------------------------\n" +
                "Số tài khoản:" + contact + "\n" +
                " \n" +
                "Tên chủ tài khoản:An a\n" +
                " \n" +
                "*Bạn có thể bỏ qua ID nếu bạn chắc rằng sẽ nhập chính xác email\n" +
                "nếu sai tiền sẽ gửi lại sau 5-10 ngày\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton("Tôi đã hiểu", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void PayRoom(double price) {

        String text = "\nHiện tiền hoa hồng bạn cần  phải trả là " + formatter.format(price) + "\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(text);
        builder.setNeutralButton("Đóng sau", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Đóng tiền ngay", (dialog, which) -> {
            if (PersonAPI.getInstance().getPoint() >= price) {
                db.collection("CreditCard").whereEqualTo("id_person", PersonAPI.getInstance().getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot value : task.getResult()) {
                                CreditCard card = value.toObject(CreditCard.class);
                                card.setPoint(card.getPoint() - price);

                                db.collection("CreditCard").document(value.getId())
                                        .set(card);

                                if (task.isComplete()) {
                                    db.collection("Commission").document(PersonAPI.getInstance().getUid())
                                            .get().addOnCompleteListener(task12 -> {
                                        Commission commission = task12.getResult().toObject(Commission.class);
                                        commission.setLastPaid(new Timestamp(new Date()));
                                        commission.setPrice(0);
                                        commission.setTotalDay(0);

                                        db.collection("Commission").document(PersonAPI.getInstance().getUid())
                                                .set(commission);

                                        if (task12.isComplete()) {
                                            db.collection("Room").whereEqualTo("person_id",
                                                    PersonAPI.getInstance().getUid())
                                                    .get().addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    for (QueryDocumentSnapshot value1 : task1.getResult()) {
                                                        Room room = value1.toObject(Room.class);

                                                        db.collection("BookRoom").whereEqualTo("id_room",
                                                                room.getRoom_id()).whereEqualTo("confirm", true)
                                                                .get().addOnCompleteListener(task2 -> {
                                                            if (task2.isSuccessful()) {
                                                                for (QueryDocumentSnapshot value2 : task2.getResult()) {
                                                                    BookRoom bookRoom = value2.toObject(BookRoom.class);
                                                                    bookRoom.setBookRoomAdded(new Timestamp(new Date()));

                                                                    db.collection("BookRoom").document(value2.getId())
                                                                            .set(bookRoom);

                                                                    if (task2.isComplete()) {
                                                                        Toast.makeText(ActivitySettingPerson.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            } else {
                AddPoint("Bạn không đủ tiền", this);
            }
        });
        builder.show();
    }

    public void ShowDialogRefund() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewLayout = getLayoutInflater().inflate(R.layout.layout_refund, null);

        builder.setView(viewLayout);

        btnCancel = viewLayout.findViewById(R.id.btn_cancel_refund);
        btnRefund = viewLayout.findViewById(R.id.btn_refund);
        edMoney = viewLayout.findViewById(R.id.ed_money_refund);


        final AlertDialog show = builder.show();

        btnCancel.setOnClickListener(v -> show.dismiss());
        btnRefund.setOnClickListener(v -> {
            double price = Double.parseDouble(edMoney.getText().toString());

            if (price <= PersonAPI.getInstance().getPoint()) {
                if (price == 0) {
                    show.dismiss();
                    return;
                }

                db.collection("Refund").whereEqualTo("id_person", PersonAPI.getInstance().getUid())
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {

                    db.collection("CreditCard").whereEqualTo("email_person", PersonAPI.getInstance().getEmail())
                            .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot value : task.getResult()) {
                                card = value.toObject(CreditCard.class);

                                card.setPoint(card.getPoint() - price);
                                PersonAPI.getInstance().setPoint(card.getPoint());

                                db.collection("CreditCard").document(value.getId())
                                        .set(card);

                                Timestamp timestamp = new Timestamp(new Date());
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
                                        Refund refund = x.toObject(Refund.class);
                                        refund.setPrice(refund.getPrice() + price);
                                        refund.setRefundAdded(timestamp);
                                        db.collection("Refund").document(x.getId())
                                                .set(refund)
                                                .addOnSuccessListener(aVoid -> {
                                                    NotificationRefund();
                                                    show.dismiss();
                                                });
                                    }
                                } else {

                                    DocumentReference refRefund = db.collection("Refund").document();

                                    Refund refund = new Refund(refRefund.getId(), PersonAPI.getInstance().getUid(), price,
                                            card.getNumber_bankcard(), timestamp);

                                    db.collection("Refund").add(refund)
                                            .addOnCompleteListener(task12 -> {
                                                NotificationRefund();
                                                show.dismiss();
                                            });
                                }
                            }
                        }
                    });

                });


            } else {
                Toast.makeText(this, "Không đủ để rút", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void NotificationRefund() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Rút tiền thành công vui lòng đợi vài ngày");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}