package com.shreeniclix.priyanka.popularmoviesapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by priyanka on 8/24/2017.
 */

public class TaskContract {
    public static final String AUTHORITY = "com.shreeniclix.priyanka.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_TASKS = "favs";

    public static final class TaskEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "favs";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTERPATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTEAVERAGE = "voteAverage";
        public static final String COLUMN_RELEASEDATE = "releaseDate";
        public static final String COLUMN_MOVIEID = "movieId";
    }
}
