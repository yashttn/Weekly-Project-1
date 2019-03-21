package com.example.sample;

public class GlobalConstants {

    //Splash Screen Timeout time
    static final int SPLASH_SCREEN_TIMEOUT = 3000;

    // Regex Pattern for Email id validation
    static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

    // Database Constants
    static final String DB_NAME = "Mocky";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME = "Mocky_Users";
    static final String COLUMN_ID = "mocky_id";
    static final String COLUMN_FIRST_NAME = "mocky_first_name";
    static final String COLUMN_LAST_NAME = "mocky_last_name";
    static final String COLUMN_AVATAR = "mocky_avatar";
    static final String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_NAME + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_FIRST_NAME + " VARCHAR(20), "
            + COLUMN_LAST_NAME + " VARCHAR(20), "
            + COLUMN_AVATAR + " TEXT );";
    static final String DROP_TABLE_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
