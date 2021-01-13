package com.example.hethongthuenha.Retrofit;

import com.example.hethongthuenha.Notification.MyResponse;
import com.example.hethongthuenha.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Context-Type:application/json",
                    "Authorization:key=AAAAbAPD4Cg:APA91bEWXUAoAYZGRMuts9ighenr1n1holFWNLl6WR1VeRlymPurGQT6AtWwr0b8CETWG-QX1-2ek2AJAaKYx4OLOV1-cjqVUT6HuiZWixf55qcWeYsLliixyb6c2QJzVWRAjrABEQxR"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
