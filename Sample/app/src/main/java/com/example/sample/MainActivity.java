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

import static com.example.sample.GlobalConstants.*;

public class MainActivity extends AppCompatActivity implements LoginScreenFragment.ILoginFragmentCommunicator {

    SharedPreferences preferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DataManager dataManager;
    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starters();

        if (isFirstTime()) {
            showSplashScreen();
        } else {
            showLoginScreen();
        }
    }

    /**
     * starters is used to intialize all the first things
     * at the onCreate level of activity
     */
    private void starters() {
        preferences = getSharedPreferences("Mocky", MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        dataManager = new DataManager();
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Used to check, is it the first time of user in the app?
     *
     * @return It returns boolean, which tells whether is it
     * the first time of user in the app or not
     */
    private boolean isFirstTime() {
        return !preferences.contains("first_time");
    }

    /**
     * If it is the first time of user in the app,
     * showSplashScreen() will show a small splash screen,
     * SPLASH_SCREEN_TIMEOUT of 5 seconds.
     */
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

    /**
     * shows login screen fragment
     */
    private void showLoginScreen() {
        LoginScreenFragment loginScreenFragment = new LoginScreenFragment();
        loginScreenFragment.setLoginFragmentCommunicator(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginScreenFragment).addToBackStack(null).commit();
    }


    private void showListUsersScreen() {
        UsersListScreenFragment usersListScreenFragment = new UsersListScreenFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, usersListScreenFragment).addToBackStack(null).commit();
    }

    /**
     * Overriden function from login screen fragment
     * sends login request
     *
     * @param email    Email sent from login screen
     * @param password Password send from login screen
     */
    @Override
    public void sendLoginDetails(String email, String password) {
        Log.v("yash", email + password);
        if (dataManager.loginUserRequest(this, email, password)) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            fragmentManager.popBackStack();
            showListUsersScreen();
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overriden function from login screen fragment
     * sends registration request
     *
     * @param email    Email sent from login screen
     * @param password Password send from login screen
     */
    @Override
    public void sendRegistrationDetails(String email, String password) {
        Log.v("yash", email + password);
        if (dataManager.registerUserRequest(this, email, password)) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        database.close();
    }
}
