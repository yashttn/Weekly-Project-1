package com.example.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.sample.GlobalConstants.*;

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

    public void insertDataInDB(SQLiteDatabase database, String employeeName, String employeePhone, String employeeAddress) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, employeeName);
        contentValues.put(COLUMN_FIRST_NAME, employeeName);
        contentValues.put(COLUMN_LAST_NAME, employeePhone);
        contentValues.put(COLUMN_AVATAR, employeeAddress);
        database.insert(TABLE_NAME, null, contentValues);
    }

    Cursor fetchDataFromDB(SQLiteDatabase database) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_AVATAR};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
