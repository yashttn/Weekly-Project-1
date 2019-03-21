package com.example.sample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("register")
    Call<ServerResponse> registerUser(@Body RegisterUserModel registerUserModel);

    @POST("login")
    Call<ServerResponse> loginUser(@Body RegisterUserModel registerUserModel);
}
