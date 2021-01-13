package com.example.hethongthuenha.Register;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hethongthuenha.Model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebase;
    private String TAG = "SIMPLE_TAG";

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();
        this.mFirebase = FirebaseFirestore.getInstance();
    }

    @Override
    public void RegisterAccount(String email, String password, String username, String contact) {


        view.registerPending();

        mFirebase.collection("User").whereEqualTo("contact", contact)
                .whereEqualTo("locked", true).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult().isEmpty()){
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task1 -> {
                                        if (task.isSuccessful()) {
                                            String id = task1.getResult().getUser().getUid();
                                            mFirebase.collection("User")
                                                    .add(new Person(id, username, email, contact, "", 1, false))
                                                    .addOnSuccessListener(documentReference -> Log.d(TAG, "onSuccess: "))
                                                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: "));
                                            view.registerSuccess();
                                        } else
                                            view.registerFail("Đăng ký thất bại !");
                                    }).addOnFailureListener(e -> view.registerFail("Đăng ký thất bại !"));
                        }else{
                            view.registerFail("Tài khoản của bạn có chứa thông tin tài khoản bị khóa !");
                        }

                    }
                });

    }

}
