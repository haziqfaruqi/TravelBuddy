package com.example.travelbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "travelbuddy.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_NAME = "travel_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_DATETIME = "datetime";
    private static final String COLUMN_LOCATION_NAME = "location_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PHOTO_LABEL = "photo_label";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_IMAGE_PATH + " TEXT, "
                + COLUMN_DATETIME + " TEXT, "
                + COLUMN_LOCATION_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_PHOTO_LABEL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }

    public void addNewRecord(String userId, String imagePath, String locationName, String description, String photoLabel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_IMAGE_PATH, imagePath);
        values.put(COLUMN_DATETIME, currentDateTime);
        values.put(COLUMN_LOCATION_NAME, locationName);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PHOTO_LABEL, photoLabel);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getRecordsByUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_ID + " DESC", new String[]{userId});
    }

    public int getRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
