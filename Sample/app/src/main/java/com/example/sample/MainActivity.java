package com.example.sample;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.add_user_dialog.AddUserDialogFragment;
import com.example.sample.add_user_dialog.IAddUserDetails;
import com.example.sample.data_manager.DataManager;
import com.example.sample.data_manager.IResponseListener;
import com.example.sample.database_helper.DBHelper;
import com.example.sample.login_screen.IDetailsListener;
import com.example.sample.login_screen.LoginScreenFragment;
import com.example.sample.models.UsersModel;
import com.example.sample.network_state_receiver.IOnNetworkStateChanged;
import com.example.sample.network_state_receiver.NetworkStateReceiver;
import com.example.sample.splash_screen.SplashScreenFragment;
import com.example.sample.user_details_screen.UserDetailsScreenFragment;
import com.example.sample.users_list_screen.IDataChangeListener;
import com.example.sample.users_list_screen.UsersListScreenFragment;

import java.util.List;

import static com.example.sample.global_constants.GlobalConstants.*;

public class MainActivity extends AppCompatActivity implements IDetailsListener, IResponseListener, IDataChangeListener, IAddUserDetails, IOnNetworkStateChanged {

    SharedPreferences preferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DataManager dataManager;
    DBHelper dbHelper;
    SQLiteDatabase database;
    LoginScreenFragment loginScreenFragment;
    UsersListScreenFragment usersListScreenFragment;
    UserDetailsScreenFragment userDetailsScreenFragment;
    NetworkStateReceiver networkStateReceiver;
    ConstraintLayout mainActivityConstraintLayout;
    int pageNo;
    boolean isConnected;

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
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.setOnNetworkStateChanged(this);    // Connecting IOnNetworkStateChanged
        mainActivityConstraintLayout = findViewById(R.id.main_activity_constraint_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNo = 1;
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
    }

    @Override
    public void onUsersListReceived(List<UsersModel> usersModelList) {
        if (fragmentManager.getFragments().size() > 0) {
            if (fragmentManager.getFragments().get(0) instanceof UsersListScreenFragment)
                usersListScreenFragment.setUsersData(usersModelList);
        }
    }

    @Override
    public void onAddUserResponseReceived(boolean response_successful, int sent_by) {
        if (sent_by == ADD_USER_REQUEST) {
            if (response_successful)
                Toast.makeText(this, "User Added Successfully!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Update User Request Failed!", Toast.LENGTH_SHORT).show();
        } else {
            if (response_successful)
                Toast.makeText(this, "User Updated Successfully!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Update User Request Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteUserResponseReceived(boolean response_successful) {
        if (response_successful)
            Toast.makeText(this, "User Deleted Successfully!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Delete User Request Failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMoreData() {
        if (pageNo <= 4) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            if (isConnected) dataManager.getUsersListRequest(pageNo++, dbHelper, database);
            else dataManager.getUsersListFromDB(pageNo++, dbHelper, database);
        }
    }

    private void makeUsersListRequest() {
        if (pageNo <= 4) {
            if (isConnected) dataManager.getUsersListRequest(pageNo++, dbHelper, database);
            else dataManager.getUsersListFromDB(pageNo++, dbHelper, database);
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
    public void addUpdateUserNameJob(int sent_by) {
        AddUserDialogFragment addUserDialogFragment = new AddUserDialogFragment();
        addUserDialogFragment.setAddUserDetails(this);  // Connecting IAddUserDetails
        Bundle bundle = new Bundle();
        bundle.putInt("sent_by", sent_by);
        addUserDialogFragment.setArguments(bundle);
        addUserDialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void deleteUserDetails(int user_id) {
        if (isConnected) dataManager.deleteUserRequest(user_id);
        else Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareUserDetails(UsersModel usersModel) {
        if (isConnected) dataManager.shareUserRequest(this, usersModel);
        else Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logoutUser() {
        preferences.edit().putBoolean("signed_in", false).apply();
        fragmentManager.popBackStack();
        showLoginScreen();
    }

    @Override
    public void onAddUserDetailsReceived(String name, String job, int sent_by) {
        dataManager.addUpdateUserRequest(name, job, sent_by);
    }

    @Override
    public void onDetailsReceived(String email, String password, int sent_by) {
        if (isConnected)
            dataManager.loginRegisterUserRequest(email, password, sent_by);
        else
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        registerReceiver(networkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        database.close();
    }

    @Override
    public void isNetworkConnected(boolean network_state) {
        isConnected = network_state;
        showNetworkSnackBar(network_state);
    }

    private void showNetworkSnackBar(final boolean network_connected) {
        String networkConnected;
        Snackbar snackbar;
        TextView snackbarText;
        if (network_connected) {
            networkConnected = "You are Back Online!!";
            snackbar = Snackbar.make(mainActivityConstraintLayout, networkConnected, Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            snackbarText = view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.argb(255, 0, 150, 100));


        } else {
            networkConnected = "No Internet Connection!";
            snackbar = Snackbar.make(mainActivityConstraintLayout, networkConnected, Snackbar.LENGTH_INDEFINITE);
            View view = snackbar.getView();
            snackbarText = view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.argb(255, 178, 34, 34));
        }
        if (fragmentManager.getFragments().size() > 0) {
            if (fragmentManager.getFragments().get(0) instanceof UsersListScreenFragment)
                makeUsersListRequest();
        }
        snackbarText.setTextSize(15);
        snackbar.show();
    }


}
