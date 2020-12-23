package com.example.hethongthuenha.API;

import com.example.hethongthuenha.Model.District;
import com.example.hethongthuenha.Model.Province;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GetDataProvinceService {

    @GET("/kenzouno1/DiaGioiHanhChinhVN/master/json/tinh.json")
    Observable<List<Province>> getAllProvince();
    @GET("/kenzouno1/DiaGioiHanhChinhVN/master/json/huyen.json")
    Observable<List<District>> getAllDistrict();
}
