package com.example.sample;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sample.data_manager.DataManager;
import com.example.sample.data_manager.IResponseListener;
import com.example.sample.database_helper.DBHelper;
import com.example.sample.login_screen.IDetailsListener;
import com.example.sample.login_screen.LoginScreenFragment;
import com.example.sample.models.UsersModel;
import com.example.sample.splash_screen.SplashScreenFragment;
import com.example.sample.user_details_screen.UserDetailsScreenFragment;
import com.example.sample.users_list_screen.IDataChangeListener;
import com.example.sample.users_list_screen.UsersListScreenFragment;

import java.util.List;

import static com.example.sample.global_constants.GlobalConstants.*;

public class MainActivity extends AppCompatActivity implements IDetailsListener, IResponseListener, IDataChangeListener {

    SharedPreferences preferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DataManager dataManager;
    DBHelper dbHelper;
    SQLiteDatabase database;
    LoginScreenFragment loginScreenFragment;
    UsersListScreenFragment usersListScreenFragment;
    UserDetailsScreenFragment userDetailsScreenFragment;
    int pageNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starters();

        if (!isFirstTime()) {
            showSplashScreen();
        } else {
            if (isSignedIn()) {
                showUsersListScreen();
            } else {
                showLoginScreen();
            }
        }
    }

    private void starters() {
        preferences = getSharedPreferences("Mocky", MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        dataManager = new DataManager();
        dataManager.setOnResponseListener(this);       // Connecting IOnDetailsListener
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    private boolean isFirstTime() {
        if (preferences.contains("first_time")) {
            return preferences.getBoolean("first_time", false);
        } else return false;
    }

    private boolean isSignedIn() {
        if (preferences.contains("signed_in")) {
            return preferences.getBoolean("signed_in", false);
        } else return false;
    }

    private void showSplashScreen() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new SplashScreenFragment()).addToBackStack(null).commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.popBackStack();
                preferences.edit().putBoolean("first_time", true).apply();
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

    private void showUsersListScreen() {
        usersListScreenFragment = new UsersListScreenFragment();
        usersListScreenFragment.setDataChangeListener(this);    // Connecting IOnDataChangeListener
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, usersListScreenFragment).addToBackStack(null).commit();

        pageNo = preferences.getInt("page_no", 1);
        if (pageNo < 4) {
            dataManager.getUsersListRequest(pageNo);
            preferences.edit().putInt("page_no", pageNo + 1).apply();
        } else {
            Log.v("yash", "pageNo" + pageNo);
        }
    }

    @Override
    public void onUsersListReceived(List<UsersModel> usersModelList) {
        usersListScreenFragment.setUsersData(usersModelList);
    }

    @Override
    public void getMoreData() {
        if (pageNo < 3) {
            pageNo = preferences.getInt("page_no", 1);
            dataManager.getUsersListRequest(pageNo);
            preferences.edit().putInt("page_no", pageNo + 1).apply();
        }
    }

    @Override
    public void showUserDetails(UsersModel usersModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("users_model", usersModel);
        userDetailsScreenFragment = new UserDetailsScreenFragment();
        userDetailsScreenFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(usersListScreenFragment);
        fragmentTransaction.add(R.id.fragment_container, userDetailsScreenFragment).addToBackStack(null).commit();
    }

    @Override
    public void onDetailsReceived(String email, String password, int sent_by) {
        dataManager.loginRegisterUserRequest(email, password, sent_by);
    }

    @Override
    public void onLoginRegisterResponseReceived(boolean response_successful, int sent_by) {
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
                preferences.edit().putBoolean("signed_in", true).apply();
                showUsersListScreen();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferences.edit().putInt("page_no", 1).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        database.close();
    }
}
