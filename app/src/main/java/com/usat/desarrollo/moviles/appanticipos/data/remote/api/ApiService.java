package com.usat.desarrollo.moviles.appanticipos.data.remote.api;

import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoRegistroResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.InformeGastoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.LoginResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.MotivoAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.RubrosResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.SedesResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.TarifaResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.TipoComprobanteResponse;

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

    @FormUrlEncoded
    @POST("rubro/listar")
    Call<RubrosResponse> getRubros(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("tipo_comprobante/listar")
    Call<TipoComprobanteResponse> getTiposComprobante(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("tarifa/viaticos")
    Call<TarifaResponse> getViaticos(
            @Field("token") String token,
            @Field("sede_id") int sede_id,
            @Field("dias") int dias
    );


    @FormUrlEncoded
    @POST("anticipo/registrar")
    Call<AnticipoRegistroResponse> getAnticipoRegistrado(
            @Field("token") String token,
            @Field("descripcion") String descripcion,
            @Field("fecha_inicio") String fecha_inicio,
            @Field("fecha_fin") String fecha_fin,
            @Field("motivo_anticipo_id") int motivo_anticipo_id,
            @Field("sede_id") int sede_id,
            @Field("docente_id") int docente_id
            );

    @FormUrlEncoded
    @POST("anticipos/docente/listar")
    Call<AnticipoListadoResponse> getAnticipoListado(
            @Field("docente_id") int docente_id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("anticipos/docente/listar/estado")
    Call<AnticipoListadoResponse> getAnticiposEstados(
            @Field("docente_id") int docente_id,
            @Field("estado") int estado,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("informe_gasto/registrar")
    Call<InformeGastoResponse> getInformeRegistrado(
            @Field("token") String token,
            @Field("anticipo_id") int anticipo_id,
            @Field("detalle_comprobantes") String detalle_comprobantes
    );

}
