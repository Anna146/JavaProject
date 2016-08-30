package com.example.asus.station;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "applicationdata";

    private static final int DATABASE_VERSION = 1;


    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table stations (_id integer primary key autoincrement, "
            + "code text not null, name text not null, latitude integer not null, longitude integer not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // метод вызывается при создании базы данных
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // метод вызывается при обновлении базы данных, например, когда вы увеличиваете номер версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        database.execSQL("DROP TABLE IF EXISTS stations");
        onCreate(database);
    }
}