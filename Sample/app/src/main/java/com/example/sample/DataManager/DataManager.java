package com.example.sample.DataManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sample.DatabaseHelper.DBHelper;
import com.example.sample.Models.MetaUsersModel;
import com.example.sample.Models.UsersModel;
import com.example.sample.RetrofitCallbacks.ApiInterface;
import com.example.sample.Models.RegisterUserModel;
import com.example.sample.Models.ServerResponseModel;
import com.example.sample.RetrofitCallbacks.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sample.GlobalConstants.GlobalConstants.FIRST_PAGE;
import static com.example.sample.GlobalConstants.GlobalConstants.LOGIN_REQUEST;
import static com.example.sample.GlobalConstants.GlobalConstants.SIGNUP_REQUEST;

public class DataManager {

    private IResponseListener onResponseListener;
    List<UsersModel> usersModelList;

    public DataManager(IResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public void loginUserRequest(String email, String password) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        RegisterUserModel loginUserModel = new RegisterUserModel(email, password);
        Call<ServerResponseModel> data = apiInterface.loginUser(loginUserModel);

        data.enqueue(new Callback<ServerResponseModel>() {
            @Override
            public void onResponse(Call<ServerResponseModel> call, Response<ServerResponseModel> response) {
                if (onResponseListener != null) {
                    onResponseListener.onResponseReceived(response.isSuccessful(), LOGIN_REQUEST);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseModel> call, Throwable t) {

            }
        });
    }

    public void registerUserRequest(String email, String password) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        RegisterUserModel loginUserModel = new RegisterUserModel(email, password);
        Call<ServerResponseModel> data = apiInterface.registerUser(loginUserModel);

        data.enqueue(new Callback<ServerResponseModel>() {
            @Override
            public void onResponse(Call<ServerResponseModel> call, Response<ServerResponseModel> response) {
                if (onResponseListener != null) {
                    onResponseListener.onResponseReceived(response.isSuccessful(), SIGNUP_REQUEST);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseModel> call, Throwable t) {

            }
        });
    }

    public void listUsers(DBHelper dbHelper, SQLiteDatabase database) {
        List<UsersModel> usersModelList = new ArrayList<>();
        Cursor cursor = dbHelper.fetchDataFromDB(database);
        if (cursor.getCount() < 3) {
            getFirstPage(dbHelper, database);
        } else {
            UsersModel usersModel;
            while (!cursor.isLast()) {
                usersModel = new UsersModel();
                usersModel.setId(cursor.getInt(0));
                usersModel.setFirstName(cursor.getString(1));
                usersModel.setLastName(cursor.getString(2));
                usersModel.setAvatar(cursor.getString(3));
                usersModelList.add(usersModel);
            }
            if (onResponseListener != null) {
                onResponseListener.onUsersListReceived(usersModelList);
            }
        }

    }

    private void getFirstPage(final DBHelper dbHelper, final SQLiteDatabase database) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        Call<MetaUsersModel> data = apiInterface.getUsersByPage(FIRST_PAGE);

        data.enqueue(new Callback<MetaUsersModel>() {
            @Override
            public void onResponse(Call<MetaUsersModel> call, Response<MetaUsersModel> response) {
                putDataIntoDB(response.body().getData(), dbHelper, database);
            }

            @Override
            public void onFailure(Call<MetaUsersModel> call, Throwable t) {

            }
        });
    }

    private void putDataIntoDB(List<UsersModel> data, DBHelper dbHelper, SQLiteDatabase database) {
        for (UsersModel model : data) {
            dbHelper.insertDataInDB(database, model.getId(), model.getFirstName(), model.getLastName(), model.getAvatar());
        }
        if (onResponseListener != null) {
            onResponseListener.onUsersListReceived(data);
        }
    }

    public void getMoreUserDetails(int totalItemsPresent) {

//        for(int i=0; i<)
        // write this method first
    }

    public List<UsersModel> getDetailsByPage(int page_no) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<MetaUsersModel> data = apiInterface.getUsersByPage(page_no);

        data.enqueue(new Callback<MetaUsersModel>() {
            @Override
            public void onResponse(Call<MetaUsersModel> call, Response<MetaUsersModel> response) {
                if (response.isSuccessful())
                    usersModelList.addAll(response.body().getData());
                else
                    usersModelList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<MetaUsersModel> call, Throwable t) {

            }
        });
        return usersModelList;
    }
}