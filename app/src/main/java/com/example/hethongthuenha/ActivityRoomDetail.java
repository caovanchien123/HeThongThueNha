package com.example.hethongthuenha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Adapter.CommentRecyclerView;
import com.example.hethongthuenha.Adapter.RoomRecyclerView;
import com.example.hethongthuenha.Adapter.SliderAdapterExample;
import com.example.hethongthuenha.Adapter.UtilitieseRecyclerView;
import com.example.hethongthuenha.Model.BookRoom;
import com.example.hethongthuenha.Model.Comment;
import com.example.hethongthuenha.Model.CreditCard;
import com.example.hethongthuenha.Model.Description_Room;
import com.example.hethongthuenha.Model.Image_Room;
import com.example.hethongthuenha.Model.LivingExpenses_Room;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Report;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.Model.SliderItem;
import com.example.hethongthuenha.Model.Utilities_Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class
ActivityRoomDetail extends AppCompatActivity {

    //Component
    private SliderView sliderView;
    private ImageView imgAvatar;
    private TextView tvTitle, tvDescription, tvPrice, tvAccommodation,
            tvAmout, tvAddress, tvArea, tvTypeRoom, tvNamePerson, tvContactPerson,
            tvWaterPrice, tvElectricityPrice, tvTvPrice, tvInternetPrice, tvParkingPrice;
    private EditText etComment, etDescriptionReport;
    private Button btnWatchMore, btnBookRoom, btnReport, btnSendReport, btnCancelReport, btnCareBookRoom;
    private LinearLayout[] linearLiving;
    private CardView cvPerson;
    //Model
    private Room room;
    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference refReport = db.collection("Report").document();
    private DocumentReference refBookRoom = db.collection("BookRoom").document();
    private DocumentReference refComment = db.collection("Comment").document();
    private List<Comment> comments;
    private RecyclerView recyclerViewComment;
    private CommentRecyclerView adapter;
    //Other
    private DecimalFormat deciFormat;
    private NumberFormat formatter;
    private Spinner spReport;
    private ProgressDialog progressDialog;
    private int count = 0;

    SliderAdapterExample adapterSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        progressDialog = new ProgressDialog(this);
        room = (Room) getIntent().getSerializableExtra("room");
        Init();
    }

    private void Slider(){

        adapterSlider = new SliderAdapterExample(this);
        sliderView.setSliderAdapter(adapterSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :

        sliderView.startAutoCycle();

    }
    private void Init() {
        linearLiving = new LinearLayout[5];
        Description_Room description_room = room.getStage1();
        LivingExpenses_Room livingExpenses_room = room.getStage2();
        Image_Room image_room = room.getStage3();
        Utilities_Room utilities_room = room.getStage4();
        sliderView=findViewById(R.id.imageSlider);
        Slider();
        //init linearlayout
        for (int i = 0; i < 5; i++) {
            String imgId = "layout_living" + (i + 1);
            int resId = getResources().getIdentifier(imgId, "id", getPackageName());
            linearLiving[i] = findViewById(resId);
        }

        tvTitle = findViewById(R.id.tv_title_detail_room);
        tvDescription = findViewById(R.id.tv_description_detail_room);
        tvAccommodation = findViewById(R.id.tv_accommodation_detail_room);
        tvPrice = findViewById(R.id.tv_price_detail_room);
        tvAmout = findViewById(R.id.tv_amout_detail_room);
        tvAddress = findViewById(R.id.tv_address_detail_room);
        tvArea = findViewById(R.id.tv_area_detail_room);
        tvTypeRoom = findViewById(R.id.tv_type_room_detail_room);
        tvNamePerson = findViewById(R.id.name_person_detail);
        tvContactPerson = findViewById(R.id.contact_person_detail);

        btnWatchMore = findViewById(R.id.btn_watchmore_detail);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        btnReport = findViewById(R.id.btnReport);
        btnCareBookRoom = findViewById(R.id.btnCareBookRoom);

        tvWaterPrice = findViewById(R.id.custom_water_price);
        tvElectricityPrice = findViewById(R.id.custom_electricity_price);
        tvInternetPrice = findViewById(R.id.custom_internet_price);
        tvTvPrice = findViewById(R.id.custom_tv_price);
        tvParkingPrice = findViewById(R.id.custom_parking_price);

        imgAvatar = findViewById(R.id.img_avater_person_room);

        deciFormat = new DecimalFormat();
        formatter = NumberFormat.getCurrencyInstance();

        comments = new ArrayList<>();

        recyclerViewComment = findViewById(R.id.commentRecyclerview);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentRecyclerView(this, comments);
        recyclerViewComment.setAdapter(adapter);

        etComment = findViewById(R.id.et_comment_detail);
        cvPerson = findViewById(R.id.cv_person_detail);

        LoadUtilities(utilities_room);
        LoadImage(image_room);
        LoadInformation(description_room);
        LoadInformPerson(room.getPerson_id());
        LoadLivingExpesens(livingExpenses_room);
        NotificationPay();
        CommentRoom();
        GoToPerson();
        ShowDialogReport();
        CheckBookRoom();
        ListBookRoom();
        ShowComment();
        CountAccommodationRoom();
    }

    private void ListBookRoom() {
        btnCareBookRoom.setOnClickListener(v -> {
            RoomRecyclerView.ShowDialogBookRoom(room, this);
        });
    }

    private void GoToPerson() {
        cvPerson.setOnClickListener(v -> {
//            Intent intent = new Intent(ActivityRoomDetail.this, ActivityPerson.class);
//            intent.putExtra("id_person", room.getPerson_id());
//            startActivity(intent);
        });
    }

    private void Prepayment() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityRoomDetail.this);
        builderSingle.setIcon(R.drawable.home);
        builderSingle.setTitle("Chọn dạng trả trước");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityRoomDetail.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Trả trước 10%");
        arrayAdapter.add("Trả trước 25%");
        arrayAdapter.add("Trả trước 50%");
        arrayAdapter.add("Trả trước 75%");
        arrayAdapter.add("Trả trước 100%");
        builderSingle.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            progressDialog.setMessage("Xin vui lòng đợi");
            int percent_repay = 0;
            switch (which) {
                case 0:
                    percent_repay = 10;
                    break;
                case 1:
                    percent_repay = 25;
                    break;
                case 2:
                    percent_repay = 50;
                    break;
                case 3:
                    percent_repay = 75;
                    break;
                case 4:
                    percent_repay = 100;
                    break;
            }

            double price_percent = (double) ((room.getStage1().getPrice() * percent_repay) / 100);

            if (price_percent <= PersonAPI.getInstance().getPoint()) {
                String description = "Trả trước " + percent_repay + "% ";
                Timestamp timestamp = new Timestamp(new Date());
                BookRoom bookRoom = new BookRoom(refBookRoom.getId(), PersonAPI.getInstance().getUid(),
                        room.getRoom_id(), description, timestamp, false, price_percent);

                db.collection("BookRoom")
                        .add(bookRoom).addOnSuccessListener(v -> {
                    db.collection("CreditCard").whereEqualTo("email_person",
                            PersonAPI.getInstance().getEmail())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                                CreditCard card = value.toObject(CreditCard.class);
                                //set point again
                                card.setPoint(card.getPoint() - price_percent);
                                PersonAPI.getInstance().setPoint(card.getPoint());
                                //update point
                                db.collection("CreditCard").document(value.getId())
                                        .set(card);
                                //update button
                                btnBookRoom.setText("Hủy đặt phòng");
                                //update notification
                                Notification notification = new Notification(PersonAPI.getInstance().getUid(),
                                        room.getPerson_id(), description.concat(room.getStage1().getTitle())
                                        , 2, timestamp);

                                db.collection("Notification").add(notification);

                                Toast.makeText(ActivityRoomDetail.this, "Bạn đã được thêm vào danh sách trả trước", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                });
            } else {
                //ActivitySettingPerson.AddPoint("Bạn không đủ tiền", this);
            }
        });
        builderSingle.show();
    }

    private void NotificationPay() {

        btnBookRoom.setOnClickListener(v -> {

            if (btnBookRoom.getText().toString().equals("Đặt phòng")) {
                    if(count==room.getStage1().getAmout()) {
                        Toast.makeText(this, "Phòng đã đầy !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityRoomDetail.this);
                builderSingle.setIcon(R.drawable.home);
                builderSingle.setTitle("Chọn hình thức thanh toán");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityRoomDetail.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("Thanh toán trả trước");
                arrayAdapter.add("Thanh toán trả sau");

                builderSingle.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

                builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                    //String strName = arrayAdapter.getItem(which);
                    switch (which) {
                        case 0:
                            Prepayment();
                            break;
                        case 1:
                            PayLater();
                            break;
                    }
                });
                builderSingle.show();
            } else {
                CancelBookRoom();
            }
        });

    }

    private void CountAccommodationRoom() {
        db.collection("BookRoom").whereEqualTo("id_room", room.getRoom_id())
                .whereEqualTo("confirm", true)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    count++;


                }

                if (task.isComplete()) {
                    tvAmout.setText("Số lượng:" + count + "/" + room.getStage1().getAmout());
                }


            }

        });
    }

    private void CancelBookRoom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo !");
        builder.setMessage("Bạn có muốn hủy đặt phòng không ?");
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {

            db.collection("CreditCard").whereEqualTo("email_person",
                    PersonAPI.getInstance().getEmail())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                        CreditCard card = value.toObject(CreditCard.class);
                        card.setPoint(card.getPoint());
                        PersonAPI.getInstance().setPoint(card.getPoint());

                        db.collection("BookRoom")
                                .whereEqualTo("id_person", PersonAPI.getInstance().getUid())
                                .whereEqualTo("id_room", room.getRoom_id())
                                .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                            if (!queryDocumentSnapshots1.isEmpty()) {
                                for (QueryDocumentSnapshot v : queryDocumentSnapshots1) {
                                    BookRoom bookRoom = v.toObject(BookRoom.class);
                                    card.setPoint(card.getPoint() + bookRoom.getPricePaid());

                                    db.collection("BookRoom").document(v.getId())
                                            .delete();
                                    btnBookRoom.setText("Đặt phòng");
                                    db.collection("CreditCard").document(value.getId())
                                            .set(card);
                                }
                            }
                        });


                        Toast.makeText(ActivityRoomDetail.this, "Bạn đã hủy danh sách trả trước", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        builder.show();
    }

    private void CheckBookRoom() {
        db.collection("BookRoom")
                .whereEqualTo("id_person", PersonAPI.getInstance().getUid())
                .whereEqualTo("id_room", room.getRoom_id())
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                btnBookRoom.setText("Đặt phòng");
            } else {
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                    BookRoom bookRoom = value.toObject(BookRoom.class);

                    if (bookRoom.isConfirm()) {
                        Timestamp now = new Timestamp(new Date());

                        Calendar calCompare1 = Calendar.getInstance();
                        Calendar calCompare2 = Calendar.getInstance();

                        calCompare1.setTime(now.toDate());
                        calCompare2.setTime(bookRoom.getBookRoomAdded().toDate());

                        calCompare1.add(Calendar.DAY_OF_MONTH, -10);
                        //kiem tra neu sau 10 ngay se tat nut huy dat phong di
                        if (calCompare1.get(Calendar.DAY_OF_YEAR) == calCompare2.get(Calendar.DAY_OF_YEAR)
                                && calCompare1.get(Calendar.MONTH) == calCompare2.get(Calendar.MONTH)
                                && calCompare1.get(Calendar.YEAR) == calCompare2.get(Calendar.YEAR)) {
                            btnBookRoom.setVisibility(View.GONE);
                        } else {
                            btnBookRoom.setText("Hủy đặt phòng");
                        }


                    } else {
                        btnBookRoom.setText("Hủy đặt phòng");
                    }
                }
            }
        });
    }


    public void ShowDialogReport() {
        btnReport.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View viewLayout = getLayoutInflater().inflate(R.layout.layout_report, null);

            builder.setView(viewLayout);

            spReport = viewLayout.findViewById(R.id.sp_report);
            etDescriptionReport = viewLayout.findViewById(R.id.et_description_report);
            btnCancelReport = viewLayout.findViewById(R.id.btn_cancel_report);
            btnSendReport = viewLayout.findViewById(R.id.btn_send_report);

            final AlertDialog show = builder.show();

            btnCancelReport.setOnClickListener(c -> show.dismiss());
            btnSendReport.setOnClickListener(c -> {
                String typeReport = spReport.getSelectedItem().toString();
                String description = etDescriptionReport.getText().toString();
                Timestamp reportAdded = new Timestamp(new Date());

                Report report = new Report(refReport.getId(), PersonAPI.getInstance().getUid(), room.getRoom_id(),
                        typeReport, description, reportAdded);

                db.collection("Report").add(report)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                                Toast.makeText(ActivityRoomDetail.this, "Gửi thành công", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ActivityRoomDetail.this, "Gửi thất  bại", Toast.LENGTH_SHORT).show();
                            show.dismiss();
                        });
            });
        });

    }


    private void PayLater() {
        db.collection("User").whereEqualTo("uid", room.getPerson_id())
                .get().addOnCompleteListener(value -> {
            if (value.isSuccessful()) {
                for (QueryDocumentSnapshot persons : value.getResult()) {
                    Person person = persons.toObject(Person.class);
//                    Intent intent = new Intent(ActivityRoomDetail.this, ActivityChat.class);
//                    intent.putExtra("toId", person.getUid());
//                    intent.putExtra("toEmail", person.getEmail());
//                    intent.putExtra("toName", person.getFullName());
//                    intent.putExtra("description_room", "Tôi muốn thuê căn nhà " + room.getStage1().getTitle());
//                    intent.putExtra("url", room.getStage3().getImagesURL().get(0));
//                    startActivity(intent);
                }
            }
        });
    }

    private void LoadInformPerson(String id_person) {
        db.collection("User").whereEqualTo("uid", id_person)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Person person = documentSnapshot.toObject(Person.class);

                    if (!person.getUrl().equals("")) {
                        Picasso.with(this)
                                .load(person.getUrl()).placeholder(R.drawable.ic_baseline_person_24)
                                .into(imgAvatar);
                    }

                    tvNamePerson.setText(person.getFullName());
                    tvContactPerson.setText("SĐT:" + person.getContact());
                }
            }
        });
    }

    private void LoadLivingExpesens(LivingExpenses_Room livingExpensesRoom) {
        tvWaterPrice.setText(FormatMoney(livingExpensesRoom.getmWater()));
        tvElectricityPrice.setText(FormatMoney(livingExpensesRoom.getmEletric()));
        tvParkingPrice.setText(FormatMoney(livingExpensesRoom.getmParkingSpace()));
        tvInternetPrice.setText(FormatMoney(livingExpensesRoom.getmInternet()));
        tvTvPrice.setText(FormatMoney(livingExpensesRoom.getmTivi()));
    }

    private String FormatMoney(double d) {
        deciFormat.setMaximumFractionDigits(1);
        return deciFormat.format(d / 1000) + "k";
    }

    private void LoadInformation(Description_Room description_room) {

        tvTitle.setText(description_room.getTitle());
        tvAddress.setText("Địa chỉ:" + description_room.getAddress());
        tvAmout.setText("Số lượng:" + description_room.getAmout());
        tvAccommodation.setText("Sức chứa:" + description_room.getAccommodation() + "/người");
        tvPrice.setText("Giá:" + formatter.format(description_room.getPrice()) + "/" + description_room.getType_date());
        tvArea.setText("Diện tích:" + description_room.getArea() + "m2");
        tvTypeRoom.setText("Loại:" + description_room.getType_room());
        tvDescription.setText("Mô tả:" + description_room.getDescription());
    }

    private void LoadUtilities(Utilities_Room utilities_room) {

        List<String> utilityLimit = new ArrayList<>();
        Utilities_Room utilitiesRoom = new Utilities_Room();
        utilitiesRoom.setDescription_utility(utilityLimit);


        for (int i = 0; i < 3; i++) {
            if (i < utilities_room.getDescription_utility().size())
                utilityLimit.add(utilities_room.getDescription_utility().get(i));
            else
                break;
        }


        RecyclerView utilitiesRecyclerView = findViewById(R.id.UtilitiesRecyclerview);
        utilitiesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        UtilitieseRecyclerView adapter = new UtilitieseRecyclerView(this, utilitiesRoom);
        utilitiesRecyclerView.setAdapter(adapter);


        if (utilities_room.getDescription_utility().size() > 3) {
            btnWatchMore.setOnClickListener(v -> {
                int sizeLimit = utilityLimit.size();
                int sizeAll = utilities_room.getDescription_utility().size();

                if (btnWatchMore.getText().toString().equals("Xem thêm...")) {
                    while (sizeLimit < sizeAll) {
                        utilityLimit.add(utilities_room.getDescription_utility().get(sizeLimit));
                        sizeLimit++;
                    }
                    adapter.notifyDataSetChanged();
                    btnWatchMore.setText("Thu gọn..");
                } else if (btnWatchMore.getText().toString().equals("Thu gọn..")) {
                    while (sizeLimit != 3) {
                        utilityLimit.remove((sizeLimit - 1));
                        sizeLimit--;
                    }
                    adapter.notifyDataSetChanged();
                    btnWatchMore.setText("Xem thêm...");
                }
            });
        } else {
            btnWatchMore.setVisibility(View.GONE);
        }
    }

    private void CommentRoom() {

        etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {

                    db.collection("Comment").whereEqualTo("id_room", room.getRoom_id())
                            .whereEqualTo("id_person", PersonAPI.getInstance().getUid())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Comment comment = new Comment(refComment.getId(), room.getRoom_id(),
                                    PersonAPI.getInstance().getUid(), v.getText().toString(),
                                    new Timestamp(new Date()));
                            db.collection("Comment").add(comment)
                                    .addOnSuccessListener(c -> {
                                        Toast.makeText(ActivityRoomDetail.this, "Bình luận thành công", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                                Comment comment = value.toObject(Comment.class);
                                comment.setText(v.getText().toString());
                                db.collection("Comment").document(value.getId())
                                        .set(comment).addOnSuccessListener(x -> {
                                    Toast.makeText(ActivityRoomDetail.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    etComment.setText("");
                                });
                            }
                        }
                    });

                    return true; // consume.
                }
            }
            return false; // pass on to other listeners.
        });
    }

    private void ShowComment() {
        db.collection("Comment").whereEqualTo("id_room", room.getRoom_id())
                .addSnapshotListener((value, error) -> {
                    comments.clear();
                    if (error == null) {
                        for (QueryDocumentSnapshot v : value) {
                            Comment comment = v.toObject(Comment.class);
                            comments.add(comment);
                        }
                        Collections.sort(comments, (o1, o2) ->
                                (int) (o2.getTime_added().getSeconds() - o1.getTime_added().getSeconds()));
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    private void LoadImage(Image_Room image_room) {
        List<SliderItem> sliderItems=new ArrayList<>();
        for(String url:image_room.getImagesURL()){
            sliderItems.add(new SliderItem("",url));
        }
        adapterSlider.renewItems(sliderItems);
    }


}