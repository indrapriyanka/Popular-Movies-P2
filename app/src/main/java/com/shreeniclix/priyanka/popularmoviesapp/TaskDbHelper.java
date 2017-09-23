package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by priyanka on 8/24/2017.
 */

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritesFinal.db";
    private static int VERSION = 1;
    SQLiteDatabase db;


    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_POSTERPATH + " TEXT, " +
                TaskContract.TaskEntry.COLUMN_OVERVIEW + " TEXT, " +
                TaskContract.TaskEntry.COLUMN_VOTEAVERAGE + " TEXT, " +
                TaskContract.TaskEntry.COLUMN_RELEASEDATE + " TEXT, " +
                TaskContract.TaskEntry.COLUMN_MOVIEID + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
