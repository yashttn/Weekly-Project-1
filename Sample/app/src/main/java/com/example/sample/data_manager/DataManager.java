package com.example.sample.data_manager;

import com.example.sample.models.MetaUsersModel;
import com.example.sample.models.UsersModel;
import com.example.sample.retrofit_callbacks.ApiInterface;
import com.example.sample.models.LoginRegisterUserModel;
import com.example.sample.models.ServerResponseModel;
import com.example.sample.retrofit_callbacks.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sample.global_constants.GlobalConstants.SIGNUP_REQUEST;

public class DataManager {

    private IResponseListener onResponseListener;

    public void loginRegisterUserRequest(String email, String password, final int sent_by) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        LoginRegisterUserModel loginUserModel = new LoginRegisterUserModel(email, password);
        Call<ServerResponseModel> data;
        if (sent_by == SIGNUP_REQUEST) {
            data = apiInterface.registerUser(loginUserModel);
        } else {
            data = apiInterface.loginUser(loginUserModel);
        }

        data.enqueue(new Callback<ServerResponseModel>() {
            @Override
            public void onResponse(Call<ServerResponseModel> call, Response<ServerResponseModel> response) {
                if (onResponseListener != null) {
                    onResponseListener.onLoginRegisterResponseReceived(response.isSuccessful(), sent_by);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseModel> call, Throwable t) {

            }
        });
    }

    public void getUsersListRequest(int page_no) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        Call<MetaUsersModel> data = apiInterface.getUsersByPage(page_no);

        data.enqueue(new Callback<MetaUsersModel>() {
            @Override
            public void onResponse(Call<MetaUsersModel> call, Response<MetaUsersModel> response) {
                if (onResponseListener != null) {
                    if (response.body() != null) {
                        onResponseListener.onUsersListReceived(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<MetaUsersModel> call, Throwable t) {

            }
        });
    }

    public void setOnResponseListener(IResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }
}