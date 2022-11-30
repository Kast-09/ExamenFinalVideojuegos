package com.example.examenfinalcalidad.services;

import android.media.Image;

import com.example.examenfinalcalidad.entities.ImageResponse;
import com.example.examenfinalcalidad.entities.Imagen;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ImageService {
    @Headers("Authorization: Client-ID 8bcc638875f89d9")
    @POST("3/image")
    Call<ImageResponse> create(@Body Imagen imagen);
}
