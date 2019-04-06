package com.example.sample.retrofit_callbacks;

import com.example.sample.models.AddUpdateUserModel;
import com.example.sample.models.MetaUsersModel;
import com.example.sample.models.LoginRegisterUserModel;
import com.example.sample.models.ServerResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    Call<ServerResponseModel> registerUser(@Body LoginRegisterUserModel loginRegisterUserModel);

    @POST("login")
    Call<ServerResponseModel> loginUser(@Body LoginRegisterUserModel loginRegisterUserModel);

    @GET("users")
    Call<MetaUsersModel> getUsersByPage(@Query("page") Integer page);

    @POST("users")
    Call<AddUpdateUserModel> addUser(@Body AddUpdateUserModel addUpdateUserModel);

    @PUT("users/2")
    Call<AddUpdateUserModel> updateUser(@Body AddUpdateUserModel addUpdateUserModel);

    @DELETE("users/{user_id}")
    Call<Void> deleteUser(@Path("user_id") Integer userId);
}
