package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lostfound.db";

    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "items";

    public DBHelper(Context context) {

        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type TEXT,"
                + "name TEXT,"
                + "phone TEXT,"
                + "description TEXT,"
                + "date TEXT,"
                + "location TEXT,"
                + "latitude REAL,"
                + "longitude REAL,"
                + "category TEXT,"
                + "imagePath TEXT,"
                + "timestamp TEXT"
                + ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public boolean insertItem(String type,
                              String name,
                              String phone,
                              String description,
                              String date,
                              String location,
                              double latitude,
                              double longitude,
                              String category,
                              String imagePath,
                              String timestamp) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("type", type);
        values.put("name", name);
        values.put("phone", phone);
        values.put("description", description);
        values.put("date", date);
        values.put("location", location);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("category", category);
        values.put("imagePath", imagePath);
        values.put("timestamp", timestamp);

        long result = db.insert(TABLE_NAME,
                null,
                values);

        return result != -1;
    }

    public ArrayList<Item> getAllItems() {

        ArrayList<Item> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME,
                null
        );

        while(cursor.moveToNext()) {

            Item item = new Item(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7),
                    cursor.getDouble(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11)
            );

            list.add(item);
        }

        cursor.close();

        return list;
    }

    public Item getSingleItem(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        if(cursor.moveToFirst()) {

            Item item = new Item(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7),
                    cursor.getDouble(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11)
            );

            cursor.close();

            return item;
        }

        return null;
    }

    public void deleteItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,
                "id=?",
                new String[]{String.valueOf(id)});
    }
}
