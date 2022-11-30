package com.example.examenfinalcalidad.services;

import com.example.examenfinalcalidad.entities.Cuenta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CuentaServices {
    @GET("cuenta")
    Call<Cuenta> finById(@Path("cuentaId") int id);

    @GET("cuenta")
    Call<List<Cuenta>> getAll();

    @POST("cuenta")
    Call<Cuenta> create(@Body Cuenta cuenta);

    @PUT("cuenta/{id}")
    Call<Cuenta> update(@Path("id") int id, @Body Cuenta cuenta);

    @DELETE("cuenta/{id}")
    Call<Cuenta> delete(@Path("id") int id);

    @GET("/cuenta/{id}/movimiento")
    Call<List<Cuenta.Movimiento>> getMovimientos(@Path("id") int id);

    @GET("/cuenta/movimiento")
    Call<List<Cuenta.Movimiento>> getAllMovimientos();

    @POST("/cuenta/{id}/movimiento")
    Call<Cuenta.Movimiento> createMovimiento(@Path("id") int id, @Body Cuenta.Movimiento movimiento);
}
