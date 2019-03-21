package com.example.sample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUserModel extends BaseResponseModel {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;

    public RegisterUserModel(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}