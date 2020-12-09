package com.example.hethongthuenha.CreateRoom.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hethongthuenha.CreateRoom.CreateRoomActivity;
import com.example.hethongthuenha.Model.Image_Room;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class fragment_image extends Fragment {


    IDataCommunication dataCommunication;

    public fragment_image() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataCommunication = (IDataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }
    }

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private ImageView[] listImgRoom = new ImageView[4];
    private Uri[] uri = new Uri[4];
    private int imageAdded = 0;
    private StorageReference mStorageRef;
    private List<String> imageUrl;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Button btnFinishStage3 = view.findViewById(R.id.btnFinishStage3);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Làm ơn đợi");
        imageUrl = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String textViewStageId = "imgRoom" + (i + 1);
            int resId = getResources().getIdentifier(textViewStageId, "id", getActivity().getPackageName());
            listImgRoom[i] = view.findViewById(resId);

            listImgRoom[i].setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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

        if (CreateRoomActivity.roomExist != null) {
            Room room = CreateRoomActivity.roomExist;
            int i = 0;
            for (String url : room.getStage3().getImagesURL()) {
                ImageView imageView = listImgRoom[i++];
                Picasso.with(getActivity()).load(url)
                        .placeholder(R.drawable.insert_image).into(imageView);
                imageView.setContentDescription("Added");
                imageUrl.add(url);
            }

        }


        btnFinishStage3.setOnClickListener(v -> {
            if (isValid()) {
                progressDialog.show();
                if (CreateRoomActivity.roomExist == null)
                    SaveImage();
                else
                    ChangeFragment();
            }

        });
        return view;
    }

    private void ChangeFragment() {
        fragment_utilities fragment = new fragment_utilities();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        dataCommunication.Image(new Image_Room(imageUrl));
        progressDialog.dismiss();
    }

    private void SaveImage() {
        StorageReference filepath;

        for (int i = 0; i < uri.length; i++) {

            filepath = mStorageRef.child("room_image").child(CreateRoomActivity.myID + "room_" + i);
            StorageReference finalFilepath = filepath;

            filepath.putFile(uri[i])
                    .addOnSuccessListener(taskSnapshot -> finalFilepath.getDownloadUrl().
                            addOnSuccessListener(uri -> {
                                imageUrl.add(uri.toString());
                                if (imageUrl.size() == 4) {
                                    ChangeFragment();
                                }
                            })).
                    addOnFailureListener(e -> Log.d("SIMPLE", "onFailure: " + e.getMessage()));

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                PickImageFromGallery();
            else
                Toast.makeText(getActivity(), "Permission deny", Toast.LENGTH_SHORT).show();
        }
    }

    public void PickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            uri[imageAdded] = data.getData();

            listImgRoom[imageAdded].setImageURI(uri[imageAdded]);

            listImgRoom[imageAdded].setContentDescription("Added");

            imageAdded++;
        }
    }

    private boolean isValid() {
        String errorImg = "";
        boolean valid = true;
        for (int i = 0; i < listImgRoom.length; i++)
            if (listImgRoom[i].getContentDescription() == null) {
                errorImg += "Bạn chưa chọn ảnh " + (i + 1) + "\n";
                valid = false;
            }

        if (!errorImg.equals(""))
            NotificationInValid(errorImg);
        return valid;
    }

    private AlertDialog NotificationInValid(String error) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Thông báo");
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();

        return alertDialog;
    }
}