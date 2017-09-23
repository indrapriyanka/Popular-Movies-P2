package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import butterknife.Bind;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by priyanka on 7/16/2017.
 */

public class MovieDetailPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MyVideoAdapter.ListItemClickListener {
    private static final int NUM_LIST_ITEMS = 10;

    ArrayList<Movie> que;

    private final String Log_Tag = MovieDetailPage.class.getSimpleName();
    private static final int LOADER_ID_VIDEO = 35;
    private static final int LOADER_ID_REVIEW = 45;
    private static final String SEARCH_QUERY_URL_EXTRA_VIDEO = "query_video";
    private static final String SEARCH_QUERY_URL_EXTRA_REVIEW = "query_review";
    private ArrayList<String> arr_video, arr_review;
    private List<String> key, key_review;
    private LoaderManager loaderManager;
    private Bundle querybundle;
    private Loader<String> githubSearchLoaderVideo, githubSearchLoaderReview, githubSearchLoader;
    private Toast mToast;
    int movieId;
    private String ReceivedPosterName, ReceivedTitle, ReceivedOverview, ReceivedRating, ReceivedReleaseDate, fTrailer, sTrailer, searchQueryUrlString_review, searchQueryUrlString_video, searchQueryUrlString, searchQueryUrlString_videoReview;
    String smovieId;
    private ShareActionProvider mShareActionProvider;
    private CoordinatorLayout coordinator_layout;

    @BindView(R.id.button)
    Button saveasfav;
    @BindView(R.id.buttonRemoveFav)
    Button removeFav;
    @BindView(R.id.rv_trailer)
    RecyclerView mtrailer;
    @BindView(R.id.img_poster)
    ImageView iv_poster;
    @BindView(R.id.movie_title)
    TextView tv_title;
    @BindView(R.id.movie_overview)
    TextView tv_overview;
    @BindView(R.id.movie_rating)
    TextView tv_rating;
    @BindView(R.id.movie_releasedate)
    TextView tv_release_date;
    @BindView(R.id.rv_review)
    RecyclerView rv_review;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_stage2);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        loaderManager = getSupportLoaderManager();

        ReceivedPosterName = intent.getStringExtra("SendPosterName");
        Picasso.with(getApplicationContext()).load(ReceivedPosterName).into(iv_poster);

        ReceivedTitle = intent.getStringExtra("SendTitle");
        tv_title.setText(ReceivedTitle);

        ReceivedOverview = intent.getStringExtra("SendOverview");
        tv_overview.setText(ReceivedOverview);

        ReceivedRating = intent.getStringExtra("SendRating");
        String text = getString(R.string.rating, ReceivedRating);
        tv_rating.setText(text);

        ReceivedReleaseDate = intent.getStringExtra("SendReleaseDate");
        String release_date = getString(R.string.release_date, ReceivedReleaseDate);
        tv_release_date.setText(release_date);

        if (intent.hasExtra("FavValue")) {
            String favKey = intent.getStringExtra("FavValue");
            Log.d("FavValue..", favKey);

            if (favKey.equals("yesFav")) {
                saveasfav.setVisibility(View.INVISIBLE);
                removeFav.setVisibility(View.VISIBLE);
            }
        }

        movieId = intent.getIntExtra("SendId", 0);
        smovieId = Integer.toString(movieId);
        mtrailer.setTag(movieId);

        favoriteTask();

        coordinator_layout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        //will have to uncomment
//        ActionBar actionBar = this.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        int ReceivedId = intent.getIntExtra("SendId", -1);
        if (ReceivedId != -1) {
            String ReceivedIdString = String.valueOf(ReceivedId);
            URL videoUrl = NetworkUtils.buildUrlVideo(ReceivedId);
            URL reviewUrl = NetworkUtils.buildUrlReview(ReceivedId);

            querybundle = new Bundle();
            querybundle.putString(SEARCH_QUERY_URL_EXTRA_VIDEO, videoUrl.toString());
            querybundle.putString(SEARCH_QUERY_URL_EXTRA_REVIEW, reviewUrl.toString());

            if (querybundle.getString(SEARCH_QUERY_URL_EXTRA_VIDEO) != "" || querybundle.getString(SEARCH_QUERY_URL_EXTRA_REVIEW) != "") {
                githubSearchLoaderVideo = loaderManager.getLoader(LOADER_ID_VIDEO);
                githubSearchLoaderReview = loaderManager.getLoader(LOADER_ID_REVIEW);

                if (githubSearchLoaderVideo == null) {
                    loaderManager.initLoader(LOADER_ID_VIDEO, querybundle, this);
                } else {
                    loaderManager.restartLoader(LOADER_ID_VIDEO, querybundle, this);
                }
                if (githubSearchLoaderReview == null) {
                    loaderManager.initLoader(LOADER_ID_REVIEW, querybundle, this);
                } else {
                    loaderManager.restartLoader(LOADER_ID_REVIEW, querybundle, this);
                }
                githubSearchLoader = loaderManager.getLoader(LOADER_ID_REVIEW);
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION",new int[]{coordinator_layout.getScrollX(),coordinator_layout.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if(position != null)
            coordinator_layout.post(new Runnable() {
                public void run() {
                    coordinator_layout.scrollTo(position[0], position[1]);
                }
            });
    }

    public void favoriteTask() {
        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(smovieId).build();
        Cursor returnCursor = getContentResolver().query(uri, null, smovieId, null, null);
        if (returnCursor.moveToFirst()) {
            saveasfav.setVisibility(View.INVISIBLE);
            removeFav.setVisibility(View.VISIBLE);
        } else {
            saveasfav.setVisibility(View.VISIBLE);
            removeFav.setVisibility(View.INVISIBLE);
        }

        //will have to uncomment
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public void addAsFav(View v) {
        Toast.makeText(getApplicationContext(), "Movie added as favorite", Toast.LENGTH_SHORT).show();
        String inputTitle = ((TextView) findViewById(R.id.movie_title)).getText().toString();

        if (inputTitle.length() == 0) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        Log.d("CONTENT_VALUES", inputTitle + " : " + ReceivedPosterName + " : " + ReceivedOverview + " : " + ReceivedRating + " : " + ReceivedReleaseDate + " : " + movieId);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TITLE, inputTitle);
        contentValues.put(TaskContract.TaskEntry.COLUMN_POSTERPATH, ReceivedPosterName);
        contentValues.put(TaskContract.TaskEntry.COLUMN_OVERVIEW, ReceivedOverview);
        contentValues.put(TaskContract.TaskEntry.COLUMN_VOTEAVERAGE, ReceivedRating);
        contentValues.put(TaskContract.TaskEntry.COLUMN_RELEASEDATE, ReceivedReleaseDate);
        contentValues.put(TaskContract.TaskEntry.COLUMN_MOVIEID, movieId);

        // below is the proper working code
        getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
        saveasfav.setVisibility(v.GONE);
        removeFav.setVisibility(v.VISIBLE);
//        finish();
    }

    public void removeAsFav(View v) {
        Toast.makeText(getApplicationContext(), "Movie Unfavorited", Toast.LENGTH_SHORT).show();
        saveasfav.setVisibility(View.VISIBLE);
        removeFav.setVisibility(View.GONE);
        String idString = Integer.toString(movieId);

        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(idString).build();
        getContentResolver().delete(uri, null, null);
    }

    @Override
    public Loader onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String cacheResult;
            String searchQueryUrlString_videoReview = "";

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                if (cacheResult != null) {
                    deliverResult(cacheResult);
                } else if (cacheResult == null) {
                    forceLoad();
                }
                if (id == LOADER_ID_VIDEO) {
                    searchQueryUrlString_videoReview = args.getString(SEARCH_QUERY_URL_EXTRA_VIDEO);
                } else if (id == LOADER_ID_REVIEW) {
                    searchQueryUrlString_videoReview = args.getString(SEARCH_QUERY_URL_EXTRA_REVIEW);
                }
            }

            @Override
            public String loadInBackground() {
                if (searchQueryUrlString_videoReview != "" && !TextUtils.isEmpty(searchQueryUrlString_videoReview)) {
                    try {
                        URL videoReviewUrl = new URL(searchQueryUrlString_videoReview);
                        String videoReviewUrl_JSON = NetworkUtils.getResponseFrpmHTTPUrl(videoReviewUrl);
                        return videoReviewUrl_JSON;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else
                    return null;
            }


            @Override
            public void deliverResult(String data) {
                super.deliverResult(data);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (loader.getId() == LOADER_ID_REVIEW) {
            if (data != null) {
                setOutputReview(data);
            } else {
                Log.d(Log_Tag, "On loadfinished retunred null. No data present");
            }
            loaderManager.destroyLoader(LOADER_ID_REVIEW);
        }
        if (loader.getId() == LOADER_ID_VIDEO) {
            if (data != null) {
                setOutputVideo(data);
            } else {
                Log.d(Log_Tag, "On loadfinished retunred null. No data present");
            }
            loaderManager.destroyLoader(LOADER_ID_VIDEO);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public void setOutputVideo(String jsonData) {
        arr_video = new ArrayList<>();
        key = new ArrayList<>();

        JSONObject video_object = null;
        try {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mtrailer.setLayoutManager(mLayoutManager);

            mtrailer.setHasFixedSize(true);
            arr_video.clear();
            video_object = new JSONObject(jsonData);
            JSONArray vresult_array = video_object.getJSONArray("results");
            for (int i = 0; i < vresult_array.length(); i++) {
                JSONObject array_object = vresult_array.getJSONObject(i);
                arr_video.add("https://www.youtube.com/watch?v=" + array_object.getString("key"));
                key.add(array_object.getString("key"));
            }
            fTrailer = arr_video.get(0);
            RecyclerView.Adapter myVideoAdapter = new MyVideoAdapter(getApplicationContext(), arr_video, key, this);
            mtrailer.setAdapter(myVideoAdapter);

            loaderManager.destroyLoader(LOADER_ID_VIDEO);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Log_Tag, "SetOutputData threw an exception");
        }

    }

    public void setOutputReview(String jsonDataReview) {
        arr_review = new ArrayList<>();
        key_review = new ArrayList<>();

        JSONObject review_object = null;
        try {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            rv_review.setLayoutManager(mLayoutManager);

            rv_review.setHasFixedSize(true);
            arr_review.clear();
            review_object = new JSONObject(jsonDataReview);
            JSONArray rresult_array = review_object.getJSONArray("results");
            for (int i = 0; i < rresult_array.length(); i++) {
                JSONObject array_object = rresult_array.getJSONObject(i);
                arr_review.add(array_object.getString("content"));
            }
            RecyclerView.Adapter myReviewAdapter = new MyReviewAdapter(getApplicationContext(), arr_review);
            rv_review.setAdapter(myReviewAdapter);
            loaderManager.destroyLoader(LOADER_ID_REVIEW);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Log_Tag, "SetOutputData threw an exception");
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex, String utubeUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(utubeUrl));
        startActivity(intent);
    }

    //may have to remove. added on last day of project submission
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    public void shareText(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Your sharing message goes here";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = fTrailer;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Trailer link.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

