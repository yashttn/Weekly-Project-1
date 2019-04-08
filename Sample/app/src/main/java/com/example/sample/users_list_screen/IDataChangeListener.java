package com.example.sample.users_list_screen;

import com.example.sample.models.UsersModel;

public interface IDataChangeListener {
    void getMoreData();

    void showUserDetails(UsersModel usersModel);

    void addUpdateUserNameJob(int sent_by);

    void deleteUserDetails(int user_id);

    void shareUserDetails(UsersModel usersModel);

    void logoutUser();

}
