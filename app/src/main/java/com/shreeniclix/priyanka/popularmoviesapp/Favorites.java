package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by priyanka on 8/27/2017.
 */

public class Favorites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FavsRecyclerAdapter.ListItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private FavsRecyclerAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_favsGrid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //2 is the number of columns
        mAdapter = new FavsRecyclerAdapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.sort,menu);
//        return true;
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    Log.d("Reached..ONSTARTLOAD", "MTASKDATA IS NOT NULL");
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    Log.d("Reached..ONSTARTLOAD", "MTASKDATA IS NULL");
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Log.d("Reached..", "starting loadinbackground for tempcursor");
                    Cursor tempCursor = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            TaskContract.TaskEntry._ID);
                    return tempCursor;
                } catch (Exception e) {
                    Log.e("Reached..", "Failed to asynchronously load data");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(int clickedItemPosition, String titleItem, String posterPathItem, String overviewItem, String voteAvgItem, String releaseDateItem, int movieIdItem) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailPage.class);
        intent.putExtra("SendPosterName", posterPathItem);
        intent.putExtra("SendTitle", titleItem);
        intent.putExtra("SendOverview", overviewItem);
        intent.putExtra("SendRating", voteAvgItem);
        intent.putExtra("SendReleaseDate", releaseDateItem);
        intent.putExtra("SendId", movieIdItem);
        intent.putExtra("FavValue", "yesFav");
        startActivity(intent);
    }
}

