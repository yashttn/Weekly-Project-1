package com.example.sample.data_manager;

import com.example.sample.Models.UsersModel;

import java.util.List;

public interface IResponseListener {
    void onResponseReceived(boolean response_successful, int sent_by);

    void onUsersListReceived(List<UsersModel> usersModelList);
}
