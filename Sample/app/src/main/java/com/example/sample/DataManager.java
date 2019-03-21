package com.example.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {

    boolean response_code;

    /**
     * Sends Login User Request to Server
     *
     * @param context  Base Activity context
     * @param email    email entered by user for login
     * @param password password entered by user for login
     */
    public boolean loginUserRequest(final Context context, String email, String password) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        RegisterUserModel loginUserModel = new RegisterUserModel(email, password);
        Call<ServerResponse> data = apiInterface.loginUser(loginUserModel);

        data.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                response_code = response.isSuccessful();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

        return response_code;
    }

    /**
     * Sends Registration User Request to Server
     *
     * @param context  Base Activity context
     * @param email    email entered by user for signup
     * @param password password entered by user for signup
     */
    public boolean registerUserRequest(final Context context, String email, String password) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        RegisterUserModel loginUserModel = new RegisterUserModel(email, password);
        Call<ServerResponse> data = apiInterface.registerUser(loginUserModel);

        data.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.v("yash", "a" + response_code);
                response_code = response.isSuccessful();
                Log.v("yash", "b" + response_code);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

        // Problem is that the retrofit is asynchronous and this
//        method returns the response code before even retro hits the api
//        Log.v("yash", "c" + response_code);
        return response_code;
    }

    public void listUsers(SQLiteDatabase db) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

    }
}