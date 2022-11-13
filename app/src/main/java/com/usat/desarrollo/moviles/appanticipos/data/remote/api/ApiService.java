package com.usat.desarrollo.moviles.appanticipos.data.remote.api;

import com.usat.desarrollo.moviles.appanticipos.data.remote.response.LoginResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.MotivoAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.SedesResponse;

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
    @FormUrlEncoded
    @POST("motivo_anticipo/listar")
    Call<MotivoAnticipoResponse> getMotivosAnticipos(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("sede/listar")
    Call<SedesResponse> getSedes(
            @Field("token") String token
    );

}
