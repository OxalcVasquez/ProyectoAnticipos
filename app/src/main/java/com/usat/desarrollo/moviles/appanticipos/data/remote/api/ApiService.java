package com.usat.desarrollo.moviles.appanticipos.data.remote.api;

import com.usat.desarrollo.moviles.appanticipos.data.remote.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> getSesion(
            @Field("email") String email,
            @Field("password") String password
    );
}
