package com.example.hethongthuenha.Login;

public interface LoginContract {
    interface Presenter {
        void LoginWithEmail(String email, String password);
    }

    interface View {
        void loginSuccess();
        void loginFail(String error);
        void loginPeding();
    }
}
