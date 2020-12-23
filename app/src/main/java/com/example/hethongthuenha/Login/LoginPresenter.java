package com.example.hethongthuenha.Login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private FirebaseAuth mAuth;


    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void LoginWithEmail(String email, String password) {
        view.loginPeding();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        view.loginSuccess();
                    else
                        view.loginFail("Đăng nhập thất bại !!!");
                }).addOnFailureListener(e -> view.loginFail("Đăng nhập thất bại !!!"));
    }

}
