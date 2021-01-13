package com.example.hethongthuenha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Adapter.ChatRecyclerView;
import com.example.hethongthuenha.Model.Chat;
import com.example.hethongthuenha.Model.HistoryChat;
import com.example.hethongthuenha.Model.Notification;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Notification.Client;
import com.example.hethongthuenha.Notification.Data;
import com.example.hethongthuenha.Notification.MyFirebaseMessaging;
import com.example.hethongthuenha.Notification.MyResponse;
import com.example.hethongthuenha.Notification.Sender;
import com.example.hethongthuenha.Notification.Token;
import com.example.hethongthuenha.Retrofit.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChat extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText edText;
    private ImageButton btnSend;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String fromEmail;
    private String toEmail;
    private List<Chat> chats;
    private CollectionReference refChat;
    private ChatRecyclerView adapter;
    private TextView tvNamePerson;
    private List<Chat> path;
    private ImageView imgAvatar, imgBack, chooseImage;
    private HistoryChat historyChat;
    private final CollectionReference refHistoryChat = db.collection("History-chat");
    private final CollectionReference refNotification = db.collection("Notification");
    private String description, url;
    private Uri selectedImage;
    private StorageReference mStorageRef;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private APIService apiService;

    public interface FireStoreCallBack {
        void onCallBack(String path);
    }

    public interface OnGetDataListener {
        void onSuccess(QuerySnapshot dataSnapshotValue);

        void onStart(String path);

        void onComplete();

        void onFailure(String error);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fromEmail = PersonAPI.getInstance().getEmail();
        toEmail = getIntent().getStringExtra("toEmail");

        Init();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("test", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = getString(R.string.msg, token);
                    Log.d("test", msg);
                });
    }


    private void Init() {
        chats = new ArrayList<>();
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRecyclerView(this, chats);
        recyclerView.setAdapter(adapter);
        edText = findViewById(R.id.edChat);
        btnSend = findViewById(R.id.btnSendChat);
        tvNamePerson = findViewById(R.id.tvNameChat);
        imgBack = findViewById(R.id.imgBackChat);
        historyChat = new HistoryChat();
        imgAvatar = findViewById(R.id.imgPersonChat);
        chooseImage = findViewById(R.id.imgChoosePicture);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        LoadInformPerson();
        btnSend.setOnClickListener(v -> {
            SendChat(HandleChat(edText.getText().toString(), ""));
        });
        chooseImage.setOnClickListener(v -> {
            showDialogChooseImage();
        });

    }


    private void SendNotification(String emailTo, String emailFrom, String text) {
        db.collection("User").whereEqualTo("email", emailTo)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty())
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                    Person person = value.toObject(Person.class);
                    db.collection("Tokens").document(person.getUid())
                            .get().addOnSuccessListener(documentSnapshot -> {
                        Token token = documentSnapshot.toObject(Token.class);


                        Data data = new Data
                                (PersonAPI.getInstance().getUid(), R.mipmap.ic_launcher, text, emailFrom, person.getUid());

                        Sender sender = new Sender(data, token.getToken());

                        Log.d("data", "SendNotification: " + token.getToken());

                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200) {
                                            if (response.body().success != 1) {
                                                Log.d("data", "onResponse: Fail");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    });
                }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        description = getIntent().getStringExtra("description_room");
        url = getIntent().getStringExtra("url");

        readData(new OnGetDataListener() {
            @Override
            public void onSuccess(QuerySnapshot dataSnapshotValue) {
                chats.clear();
                for (QueryDocumentSnapshot value : dataSnapshotValue) {
                    chats.add(value.toObject(Chat.class));
                }
            }

            @Override
            public void onStart(String path) {
                refChat = db.collection(path);
            }


            @Override
            public void onComplete() {
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chats.size() - 1);
                GetInFormRoomDetail();
            }

            @Override
            public void onFailure(String error) {
                Log.d("error", "onFailure: " + error);
            }
        });
    }

    private void showDialogChooseImage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View viewLayout = getLayoutInflater().inflate(R.layout.layout_choose_image, null);
        builder.setView(viewLayout);

        Button benGallery = viewLayout.findViewById(R.id.layout_choose_gallary);
        Button btnCamera = viewLayout.findViewById(R.id.layout_choose_camera);

        final AlertDialog show = builder.show();
        benGallery.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

            show.dismiss();
        });

        btnCamera.setOnClickListener(v -> {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, PICK_IMAGE_CAMERA);
            show.dismiss();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            UpdateImageToFirebase(selectedImage);
        } else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            UpdateImageBitmapToFirebase(bytes.toByteArray());
        }
    }


    private void UpdateImageBitmapToFirebase(byte[] image) {
        String path = "Chat/" + refChat.getPath() + "/" + UUID.randomUUID();
        mStorageRef.child(path)
                .putBytes(image)
                .addOnSuccessListener(taskSnapshot -> mStorageRef.child(path).getDownloadUrl().
                        addOnSuccessListener(url -> {
                            SendChat(HandleChat("", url.toString()));
                        })).
                addOnFailureListener(e -> Log.d("SIMPLE", "onFailure: " + e.getMessage()));
    }

    private void UpdateImageToFirebase(Uri uri) {
        String path = "Chat/" + refChat.getPath() + "/" + UUID.randomUUID();
        mStorageRef.child(path)
                .putFile(uri)
                .addOnSuccessListener(taskSnapshot -> mStorageRef.child(path).getDownloadUrl().
                        addOnSuccessListener(url -> {
                            SendChat(HandleChat("", url.toString()));
                        })).
                addOnFailureListener(e -> Log.d("SIMPLE", "onFailure: " + e.getMessage()));
    }

    private void LoadInformPerson() {
        db.collection("User").whereEqualTo("email", toEmail)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Person person = documentSnapshot.toObject(Person.class);

                    if (!person.getUrl().equals("")) {
                        Picasso.with(this)
                                .load(person.getUrl()).placeholder(R.mipmap.ic_launcher)
                                .into(imgAvatar);
                    }
                    tvNamePerson.setText(person.getFullName());

                    imgAvatar.setOnClickListener(v -> {
                        Intent intent = new Intent(ActivityChat.this, ActivityPerson.class);
                        intent.putExtra("id_person", person.getUid());
                        startActivity(intent);
                    });
                }
            }
        });
    }


    public void readData(OnGetDataListener listener) {
        FindPath(path -> {
            listener.onStart(path);
            refChat.orderBy("chatAdded").limitToLast(10).addSnapshotListener((value, error) -> {
                if (error == null) {
                    listener.onSuccess(value);
                    listener.onComplete();
                } else
                    listener.onFailure(error.getMessage());
            });
        });

    }


    private void GetInFormRoomDetail() {
        //add last chat
        if (description != null) {
            SendChat(HandleChat(description, url));
            String uid = getIntent().getStringExtra("toId");
            Timestamp notificationAdded = new Timestamp(new Date());
            Notification notification = new Notification(PersonAPI.getInstance().getUid(), uid, description, 1, notificationAdded);

            refNotification.add(notification);

            description = null;
        }
    }


    private void FindPath(final FireStoreCallBack fireStoreCallBack) {
        db.collection("Chat(" + fromEmail + "-" + toEmail + ")").
                addSnapshotListener((value, error) -> {
                    path = new ArrayList<>();
                    if (error == null) {
                        for (QueryDocumentSnapshot query : value) {
                            path.add(query.toObject(Chat.class));
                        }
                        if (path.size() != 0)
                            fireStoreCallBack.onCallBack("Chat(" + fromEmail + "-" + toEmail + ")");
                        else
                            fireStoreCallBack.onCallBack("Chat(" + toEmail + "-" + fromEmail + ")");
                    }
                });
    }

    private Chat HandleChat(String text, String url) {

        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(url))
            return null;

        Chat chat = new Chat();
        chat.setText(text);
        chat.setUrl(url);
        chat.setFrom_email_person(fromEmail);
        chat.setTo_email_person(toEmail);
        chat.setChatAdded(new Timestamp(new Date()));

        return chat;
    }

    private void SendChat(Chat chat) {
        if (chat != null) {
            historyChat.setPathChat(refChat.getPath());
            historyChat.setFromATo(tvNamePerson.getText().toString() + "-" + PersonAPI.getInstance().getName());
            if (chats.isEmpty()) {
                historyChat.setChatAdded(new Timestamp(new Date()));
                historyChat.setLastChat(chat.getText());
                refHistoryChat.add(historyChat);
            } else {
                chats.size();
                historyChat.setChatAdded(new Timestamp(new Date()));
                historyChat.setLastChat(chat.getText());
                refHistoryChat.whereEqualTo("pathChat", historyChat.getPathChat())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            refHistoryChat.document(documentSnapshot.getId())
                                    .set(historyChat);
                        }
                    }
                });
            }

            refChat.add(chat)
                    .addOnSuccessListener(documentReference -> {
                        SendNotification(toEmail, PersonAPI.getInstance().getEmail(), chat.getText());
                    });
            edText.setText("");
        }
    }
}

