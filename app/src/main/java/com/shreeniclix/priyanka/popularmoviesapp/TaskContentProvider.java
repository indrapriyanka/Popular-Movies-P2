package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by priyanka on 8/25/2017.
 */

public class TaskContentProvider extends ContentProvider {
    private TaskDbHelper mDbHelper;
    private static final int FAVS = 100;
    private static final int FAVS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, FAVS);
        //SINGLE ITEM
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", FAVS_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new TaskDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAVS:
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "movieId=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI.." + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVS:
                long id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.BASE_CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into the DB " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String id = uri.getPathSegments().get(1);
        String mSelection = "movieId=?";
        String[] mSelectionArgs = new String[]{id};

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        int deletedCount;

        switch (match)
        {
            case FAVS_WITH_ID:
                deletedCount = db.delete(TaskContract.TaskEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                if(deletedCount >0)
                {
                    Log.d("Rows deleted : ",deletedCount+"");
                }
                break;

            default:
                throw new UnsupportedOperationException("Failed to delete : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return deletedCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
