package com.example.hethongthuenha.Register;

import com.google.firebase.firestore.QuerySnapshot;

public interface RegisterContract {

    interface Presenter{
        void RegisterAccount(String email, String password, String username, String contact);
    }

    interface View{
        void registerSuccess();
        void registerFail(String error);
        void registerPending();
    }
}
