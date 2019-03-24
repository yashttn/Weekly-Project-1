package com.example.sample.RetrofitCallbacks;

import com.example.sample.Models.MetaUsersModel;
import com.example.sample.Models.RegisterUserModel;
import com.example.sample.Models.ServerResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    Call<ServerResponseModel> registerUser(@Body RegisterUserModel registerUserModel);

    @POST("login")
    Call<ServerResponseModel> loginUser(@Body RegisterUserModel registerUserModel);

    @GET("users")
    Call<MetaUsersModel> getUsersByPage(@Query("page") Integer page);
}
