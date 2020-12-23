package com.example.hethongthuenha.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hethongthuenha.R;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    private static final String TAG = "simple_test";
    private RegisterPresenter presenter;
    private ProgressDialog progressDialog;
    //private Spinner spProvince,spDistrict;
    private EditText etName,etEmail,etPassword,etRepassword,etContact;
    private Button btnRegister,btnHaveAlreadyAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        presenter=new RegisterPresenter(this);
        //spProvince=findViewById(R.id.registerProvinces);
        //spDistrict=findViewById(R.id.registerDistrict);
        //presenter.InitProvince();
        etName=findViewById(R.id.registerUsername);
        etContact=findViewById(R.id.registerContact);
        etEmail=findViewById(R.id.registerEmail);
        etPassword=findViewById(R.id.registerPassword);
        etRepassword=findViewById(R.id.registerRepassword);

        btnHaveAlreadyAccount=findViewById(R.id.btnHaveAccountReady);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v->RegisterAccount());
    }

    private void RegisterAccount(){
        String name=etName.getText().toString();
        String contact=etContact.getText().toString();
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();
        String repassword=etRepassword.getText().toString();
        if(password.equals(repassword)) {
            presenter.RegisterAccount(email, password, name, contact);
        }else
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void registerSuccess() {
        progressDialog.dismiss();
    }

    @Override
    public void registerFail(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerPending() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Đang chờ xử lý");
        progressDialog.show();
    }

//    @Override
//    public void repositoryProvince(List<Province> provinces) {
//        ArrayList<String> nameProvince=new ArrayList<>();
//
//        for (Province province:provinces) {
//            nameProvince.add(province.getName());
//        }
//
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item,
//                nameProvince);
//
//        spProvince.setAdapter(adapter);
//
//        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                presenter.InitDistrict(provinces.get(position).getId());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }
//
//    @Override
//    public void repositoryDistrict(List<District> districts) {
//        ArrayList<String> nameDistrict=new ArrayList<>();
//
//        for (District district:districts) {
//            nameDistrict.add(district.getName());
//        }
//
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item,
//                nameDistrict);
//
//        spDistrict.setAdapter(adapter);
//    }
}