package com.shreeniclix.priyanka.popularmoviesapp;

/**
 * Created by priyanka on 8/2/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private GridView gv_posters;
    private ImageView iv_movieimg;
    private TextView tv_jsonresult;
    static TextView tv_review;
    public String[] result_path;
    private ProgressDialog pDialog;
    private ArrayList<CustomGrid> arrayList;
    private int current_position;
    private int posi, scroll_position;
    private static String sortString, sort_selected;
    private ArrayList<CustomGrid> arraylistForReview;
    CustomAdapter adapter;
    String s;
    private static final int LOADER_ID = 22;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private String favsMenuItemClicked;
    ConnectivityManager cm;
    NetworkInfo activeNetworkInfo = null;
    LinearLayout ll_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        posi = 0;

        arrayList = new ArrayList<>();
        Context context = getApplicationContext();
        gv_posters = (GridView) findViewById(R.id.gv_posters);
        tv_review = new TextView(this);
        TextView tv_review = (TextView) findViewById(R.id.movie_review);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Bundle querybundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();

        if (savedInstanceState == null) {
            URL apiUrl = NetworkUtils.buildURL("now_playing");

            querybundle.putString(SEARCH_QUERY_URL_EXTRA, apiUrl.toString());
            Loader<String> githubSearchLoader = loaderManager.getLoader(LOADER_ID);

            loaderManager.initLoader(LOADER_ID, querybundle, this);


            //Below is written for screen rotation and to maintain scroll position
        } else if (savedInstanceState != null && sortString != "popular" && sortString != "top_rated") {
            URL apiUrl = NetworkUtils.buildURL("now_playing");

            querybundle.putString(SEARCH_QUERY_URL_EXTRA, apiUrl.toString());
            Loader<String> githubSearchLoader = loaderManager.getLoader(LOADER_ID);
            loaderManager.initLoader(LOADER_ID, querybundle, this);
            posi = savedInstanceState.getInt("current_position"); //getting current position if scrolled

        } else if (savedInstanceState != null && ((sortString == "popular") || (sortString == "top_rated"))) {
//            sortString = savedInstanceState.getString("sort_string");
            sortString = mPref.getString("sort", "");

            URL apiUrl = NetworkUtils.buildURL(sortString);
            querybundle.putString(SEARCH_QUERY_URL_EXTRA, apiUrl.toString());
            Loader<String> githubSearchLoader = loaderManager.getLoader(LOADER_ID);
            loaderManager.initLoader(LOADER_ID, querybundle, this);
            posi = savedInstanceState.getInt("current_position"); //getting current position if scrolled

        }
        
    }

    private void makeSearchByPopular() {
        String githubQuery = "popular";
        URL searchByPopularURL = NetworkUtils.buildURL(githubQuery);

        Bundle querybundle = new Bundle();
        querybundle.putString(SEARCH_QUERY_URL_EXTRA, searchByPopularURL.toString());


        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(LOADER_ID);

        if (githubSearchLoader == null) {
            loaderManager.initLoader(LOADER_ID, querybundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, querybundle, this);

        }
    }

    private void makeSearchByTopRated() {
        String githubQuery = "top_rated";
        URL searchByTopRatedURL = NetworkUtils.buildURL(githubQuery);

        Bundle querybundle = new Bundle();
        querybundle.putString(SEARCH_QUERY_URL_EXTRA, searchByTopRatedURL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(LOADER_ID);

        if (githubSearchLoader == null) {
            loaderManager.initLoader(LOADER_ID, querybundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, querybundle, this);
        }
    }

    private void makeSearchByFavorite() {
        Intent i = new Intent(this, Favorites.class);
        startActivity(i);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String cacheResult;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                if (cacheResult != null) {
                    deliverResult(cacheResult);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }
                try {
                    URL githubUrl = new URL(searchQueryUrlString);
                    return NetworkUtils.getResponseFrpmHTTPUrl(githubUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                cacheResult = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<String> loader, final String data) {
        ConnectivityManager cm;
        NetworkInfo activeNetworkInfo = null;

        try {
            cm = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            activeNetworkInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.e("connectivity..CHECK", e.toString());
        }
        if (activeNetworkInfo != null) {
            //INTERNET CONNECTED
            if (data != null) {   //check for data is present to display
                setOutputData(data);
            } else {
                Snackbar snackbar = Snackbar.make(ll_parent, "No Data to Retrieve", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        } else { //Interent not connected
            Snackbar snackbar = Snackbar.make(ll_parent, "No Interenet Connection.", Snackbar.LENGTH_LONG);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoadFinished(loader, data);
                }
            });
            snackbar.show();

        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void setOutputData(String content) {
        arrayList.clear();
        JSONObject movie = null;
        try {
            movie = new JSONObject(content);
            JSONArray moviePoster = movie.getJSONArray("results");

            for (int i = 0; i < moviePoster.length(); i++) {
                JSONObject movieResult = moviePoster.getJSONObject(i);
                Log.d("TAG...MAINACTIVITY", "" + movieResult.getString("poster_path"));
                arrayList.add(new CustomGrid(getApplicationContext(), "http://image.tmdb.org/t/p/w185/" + movieResult.getString("poster_path"), movieResult.getString("title"), movieResult.getString("overview"), movieResult.getString("vote_average"), movieResult.getString("release_date"), movieResult.getInt("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.grid_single, arrayList);
        gv_posters.setAdapter(adapter);
//        gv_posters.smoothScrollToPosition(posi - 2);

        if (arrayList.size() > 0) {
            gv_posters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomGrid movieclick = adapter.getItem(position);

                    Intent intent = new Intent(getApplicationContext(), MovieDetailPage.class);
                    intent.putExtra("SendPosterName", movieclick.getPosterName().toString());
                    intent.putExtra("SendTitle", movieclick.gettitle().toString());
                    intent.putExtra("SendOverview", movieclick.getOverview().toString());
                    intent.putExtra("SendRating", movieclick.getRating().toString());
                    intent.putExtra("SendReleaseDate", movieclick.getRelease_date().toString());
                    intent.putExtra("SendId", movieclick.getId());
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int menuItemThatWasSelected = item.getItemId();
        posi = 0;
        if (menuItemThatWasSelected == R.id.menu_popular) {
            sortString = "popular";
            editor.putString("sort", sortString);
            editor.apply();
            makeSearchByPopular();
        } else if (menuItemThatWasSelected == R.id.menu_toprated) {
            sortString = "top_rated";
            editor.putString("sort", sortString);
            editor.apply();
            makeSearchByTopRated();
        } else if (menuItemThatWasSelected == R.id.menu_favorites) {
            sortString = "favorites";
            editor.putString("sort", sortString);
            editor.apply();
            makeSearchByFavorite();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { //on screen rotation
        super.onSaveInstanceState(outState);
        current_position = gv_posters.getLastVisiblePosition();
        outState.putInt("current_position", current_position);
        outState.putString("sort_string", sortString);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ ll_parent.getScrollX(), ll_parent.getScrollY()});
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            ll_parent.post(new Runnable() {
                public void run() {
                    ll_parent.scrollTo(position[0], position[1]);
                }
            });
    }

}
