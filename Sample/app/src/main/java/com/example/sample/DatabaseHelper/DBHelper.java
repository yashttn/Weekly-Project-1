package com.example.sample.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.sample.GlobalConstants.GlobalConstants.*;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COMMAND);
        onCreate(db);
    }

    public void insertDataInDB(SQLiteDatabase database, int userId, String userFirstName, String userLastname, String userAvatar) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, userId);
        contentValues.put(COLUMN_FIRST_NAME, userFirstName);
        contentValues.put(COLUMN_LAST_NAME, userLastname);
        contentValues.put(COLUMN_AVATAR, userAvatar);
        database.insert(TABLE_NAME1, null, contentValues);
    }

    public Cursor fetchDataFromDB(SQLiteDatabase database) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_AVATAR};
        Cursor cursor = database.query(TABLE_NAME1, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
