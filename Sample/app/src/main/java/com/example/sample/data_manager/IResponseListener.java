package com.example.sample.data_manager;

import com.example.sample.models.UsersModel;

import java.util.List;

public interface IResponseListener {
    void onLoginRegisterResponseReceived(boolean response_successful, int sent_by);

    void onUsersListReceived(List<UsersModel> usersModelList);

    void onAddUserResponseReceived(boolean response_successful, int sent_by);

    void onDeleteUserResponseReceived(boolean response_successful);
}
