package com.example.sample;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sample.data_manager.DataManager;
import com.example.sample.data_manager.IResponseListener;
import com.example.sample.database_helper.DBHelper;
import com.example.sample.LoginScreen.IDetailsListener;
import com.example.sample.LoginScreen.LoginScreenFragment;
import com.example.sample.Models.UsersModel;
import com.example.sample.SplashScreen.SplashScreenFragment;
import com.example.sample.UsersListScreen.IDataChangeListener;
import com.example.sample.UsersListScreen.UsersListScreenFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.sample.GlobalConstants.GlobalConstants.*;

public class MainActivity extends AppCompatActivity implements IDetailsListener, IResponseListener, IDataChangeListener {

    SharedPreferences preferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DataManager dataManager;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Bundle bundle;
    LoginScreenFragment loginScreenFragment;
    UsersListScreenFragment usersListScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstTime()) {
            showSplashScreen();
        } else {
            showLoginScreen();
        }
    }

    private void starters() {
        preferences = getSharedPreferences("Mocky", MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        dataManager = new DataManager(this);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        ConstraintLayout coordinatorLayout = findViewById(R.id.constraint_layout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Hey! Yashwant", Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    private boolean isFirstTime() {
        return !preferences.contains("first_time");
    }

    private void showSplashScreen() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new SplashScreenFragment()).addToBackStack(null).commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.popBackStack();
                preferences.edit().putBoolean("first_time", false).apply();
                showLoginScreen();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }

    private void showLoginScreen() {
        loginScreenFragment = new LoginScreenFragment();
        loginScreenFragment.setDetailsListener(this);   // Connecting IOnDetailsListener
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginScreenFragment).addToBackStack(null).commit();

    }

    private void makeListUsersRequest() {
        dataManager.listUsers(dbHelper, database);
    }

    private void showListUsersScreen() {
        usersListScreenFragment = new UsersListScreenFragment();
        usersListScreenFragment.setDataChangeListener(this);
        if (bundle != null) {
            usersListScreenFragment.setArguments(bundle);
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, usersListScreenFragment).addToBackStack(null).commit();
    }

    @Override
    public void onDetailsReceived(String email, String password, int sent_by) {
        if (sent_by == SIGNUP_REQUEST) {
            dataManager.registerUserRequest(email, password);
        } else {
            dataManager.loginUserRequest(email, password);
        }
    }

    // IResponseListener
    @Override
    public void onResponseReceived(boolean response_successful, int sent_by) {
        if (sent_by == SIGNUP_REQUEST) {
            if (response_successful) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (sent_by == LOGIN_REQUEST) {
            if (response_successful) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                makeListUsersRequest();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // IResponseListener
    @Override
    public void onUsersListReceived(List<UsersModel> usersModelList) {
        bundle = new Bundle();
        bundle.putParcelableArrayList("users_list", (ArrayList<? extends Parcelable>) usersModelList);
        showListUsersScreen();

    }

    @Override
    public void getMoreData(int totalItemsPresent) {
        dataManager.getMoreUserDetails(totalItemsPresent);
    }
}
