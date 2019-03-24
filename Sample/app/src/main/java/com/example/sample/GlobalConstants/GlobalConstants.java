package com.example.sample.GlobalConstants;

public class GlobalConstants {

    //Splash Screen Timeout time
    public static final int SPLASH_SCREEN_TIMEOUT = 3000;

    // Regex Pattern for Email id validation
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

    // Database Constants
    public static final String DB_NAME = "Mocky";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME1 = "mocky_users";
    public static final String COLUMN_ID = "mocky_id";
    public static final String COLUMN_FIRST_NAME = "mocky_first_name";
    public static final String COLUMN_LAST_NAME = "mocky_last_name";
    public static final String COLUMN_AVATAR = "mocky_avatar";
    public static final String COLUMN_PAGE = "mocky_avatar";
    public static final String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_NAME1 + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_FIRST_NAME + " VARCHAR(20), "
            + COLUMN_LAST_NAME + " VARCHAR(20), "
            + COLUMN_AVATAR + " TEXT );";
    public static final String DROP_TABLE_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME1;

    // Login and Registration page Constants
    public static final int LOGIN_REQUEST = 1;
    public static final int SIGNUP_REQUEST = 2;

    // Page No.s
    public static final int FIRST_PAGE = 1;
    public static final int SECOND_PAGE = 2;
    public static final int THIRD_PAGE = 3;
    public static final int FOUTRTH_PAGE = 4;
}
