package com.shreeniclix.priyanka.popularmoviesapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by priyanka on 5/25/2017.
 */

public class NetworkUtils extends AppCompatActivity{
    final static String BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String API_KEY_VALUE = "";
    final static String PARAM_QUERY = "sort_by";
//    final static String PARAM_SORT = ".desc";
    final static String API_KEY = "api_key";


    public static URL buildURL(String text) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(text)
               // .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e) {
        e.printStackTrace();
        }
        return url;
        }

        public static URL buildUrlVideo (int id)
        {
            Uri builtUriVideo = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath(String.valueOf(id))
                    .appendEncodedPath("videos")
                    .appendQueryParameter(API_KEY,API_KEY_VALUE)
                    .build();
            URL url = null;
            try
            {
                url = new URL(builtUriVideo.toString());
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;
        }
        public static URL buildUrlReview (int id)
        {
            Uri builtUrlReview = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath(String.valueOf(id))
                    .appendEncodedPath("reviews")
                    .appendQueryParameter(API_KEY,API_KEY_VALUE)
                    .build();
            URL url = null;
            try {
                url = new URL(builtUrlReview.toString());
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;
        }

        public static String getResponseFrpmHTTPUrl(URL url) throws IOException {
            Log.d("NETWORK_OPERATION", "GET RESPONSE FROM HTTP CALLED");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        return scanner.next();
                    } else {
                        return null;
                    }
                } finally {
                    urlConnection.disconnect();
                }
            }

    }