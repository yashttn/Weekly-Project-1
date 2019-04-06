package com.example.sample.users_list_screen;

import android.graphics.Bitmap;

import com.example.sample.models.UsersModel;

public interface IUserDetailsListener {
    void userDetailsClicked(UsersModel usersModel);

    void deleteUser(int user_id);

    void shareUser(UsersModel usersModel);
}
