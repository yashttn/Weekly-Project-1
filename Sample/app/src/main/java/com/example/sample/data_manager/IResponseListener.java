package com.example.sample.data_manager;

import com.example.sample.models.UsersModel;

import java.util.List;

public interface IResponseListener {
    void onLoginRegisterResponseReceived(boolean response_successful, int sent_by);

    void onUsersListReceived(List<UsersModel> usersModelList);
}
