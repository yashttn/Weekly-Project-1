package com.example.sample.data_manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sample.database_helper.DBHelper;
import com.example.sample.models.AddUpdateUserModel;
import com.example.sample.models.MetaUsersModel;
import com.example.sample.models.UsersModel;
import com.example.sample.retrofit_callbacks.ApiInterface;
import com.example.sample.models.LoginRegisterUserModel;
import com.example.sample.models.ServerResponseModel;
import com.example.sample.retrofit_callbacks.RetrofitClientInstance;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sample.global_constants.GlobalConstants.ADD_USER_REQUEST;
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

    public void getUsersListRequest(int page_no, final DBHelper dbHelper, final SQLiteDatabase database) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        Call<MetaUsersModel> data = apiInterface.getUsersByPage(page_no);

        data.enqueue(new Callback<MetaUsersModel>() {
            @Override
            public void onResponse(Call<MetaUsersModel> call, Response<MetaUsersModel> response) {
                if (onResponseListener != null) {
                    if (response.body() != null) {
                        dbHelper.insertDataIntoDB(database, response.body().getData(), response.body().getPage());
                        onResponseListener.onUsersListReceived(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<MetaUsersModel> call, Throwable t) {

            }
        });
    }

    public void getUsersListFromDB(int page_no, DBHelper dbHelper, SQLiteDatabase database) {
        Cursor cursor = dbHelper.fetchDataFromDB(database);
        int databasePageCount = cursor.getCount() / 3;
        List<UsersModel> usersModelList = new ArrayList<>();
        if (databasePageCount >= page_no) {
            UsersModel usersModel;
            while (!cursor.isLast()) {
                if (cursor.getInt(4) == page_no) {
                    usersModel = new UsersModel();
                    usersModel.setId(cursor.getInt(0));
                    usersModel.setFirstName(cursor.getString(1));
                    usersModel.setLastName(cursor.getString(2));
                    usersModel.setAvatar(cursor.getString(3));
                    usersModelList.add(usersModel);
                }
                cursor.moveToNext();
            }
            if (cursor.getInt(4) == page_no) {
                usersModel = new UsersModel();
                usersModel.setId(cursor.getInt(0));
                usersModel.setFirstName(cursor.getString(1));
                usersModel.setLastName(cursor.getString(2));
                usersModel.setAvatar(cursor.getString(3));
                usersModelList.add(usersModel);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onUsersListReceived(usersModelList);
        }
    }

    public void addUpdateUserRequest(String name, String job, final int sent_by) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        AddUpdateUserModel addUpdateUserModel = new AddUpdateUserModel();
        addUpdateUserModel.setName(name);
        addUpdateUserModel.setJob(job);
        Call<AddUpdateUserModel> data;
        if (sent_by == ADD_USER_REQUEST) {
            data = apiInterface.addUser(addUpdateUserModel);
        } else {
            data = apiInterface.updateUser(addUpdateUserModel);
        }

        data.enqueue(new Callback<AddUpdateUserModel>() {
            @Override
            public void onResponse(Call<AddUpdateUserModel> call, Response<AddUpdateUserModel> response) {
                if (onResponseListener != null) {
                    onResponseListener.onAddUserResponseReceived(response.isSuccessful(), sent_by);
                }
            }

            @Override
            public void onFailure(Call<AddUpdateUserModel> call, Throwable t) {

            }
        });
    }

    public void setOnResponseListener(IResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public void deleteUserRequest(int user_id, DBHelper dbHelper, SQLiteDatabase database) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        Call<Void> data = apiInterface.deleteUser(user_id);

        data.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (onResponseListener != null) {
                    onResponseListener.onDeleteUserResponseReceived(response.isSuccessful());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

        dbHelper.deleteUserFromDB(user_id, database);
    }

    public void shareUserRequest(Context context, UsersModel usersModel) {
        File imageFile = getOutputMediaFile(context, usersModel);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider", imageFile));
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Send to"));
    }

    private File getOutputMediaFile(final Context context, UsersModel usersModel) {
        File mediaStorageDir1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!mediaStorageDir1.exists()) {
            if (!mediaStorageDir1.mkdirs()) {
                return null;
            }
        }
        final File imageFile = new File(mediaStorageDir1.getPath() + File.separator +
                "Img_" + usersModel.getId() + "_" +
                usersModel.getFirstName() + "_" + usersModel.getLastName() + ".jpg");

        Glide.with(context).asFile().
                load(usersModel.getAvatar())
                .into(new CustomTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        try {
                            FileUtils.copyFile(resource, imageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        return imageFile;
    }
}