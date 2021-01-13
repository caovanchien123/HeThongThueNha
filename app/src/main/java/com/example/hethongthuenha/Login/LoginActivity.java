package com.example.hethongthuenha.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.example.hethongthuenha.Register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private LoginPresenter loginPresenter;
    private ProgressDialog progressDialog;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        etEmail = findViewById(R.id.loginEmail);
        etPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loginPresenter = new LoginPresenter(this);

        btnLogin.setOnClickListener(v -> HandlingLogin());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void HandlingLogin() {
        if(PersonAPI.getInstance()!=null && PersonAPI.getInstance().isLocked())
            locked(this);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loginPresenter.LoginWithEmail(email, password);
        }
    }

    @Override
    public void loginSuccess() {
        progressDialog.dismiss();
    }

    public static void locked(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Tài khoản của bạn đã bị khóa muốn biết chi tiết xin liên hệ ******");
        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            mAuth.signOut();
        });
        builder.show();
    }

    @Override
    public void loginFail(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginPeding() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang chờ xử lý");
        progressDialog.show();
    }

}