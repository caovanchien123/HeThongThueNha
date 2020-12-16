package com.example.hethongthuenha.Register;

public interface RegisterContract {

    interface Presenter{
        void RegisterAccount(String email, String password, String username, String contact);
        //void InitProvince();
        //void InitDistrict(int id_tinh);
    }

    interface View{
        void registerSuccess();
        void registerFail(String error);
        void registerPending();
//        void repositoryProvince(List<Province> provinces);
//        void repositoryDistrict(List<District> districts);
    }
}
