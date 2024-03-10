package com.example.musicapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("ikara-storage/ikara/lyrics.xml")
    Call<YourXMLModel> getData();
}
