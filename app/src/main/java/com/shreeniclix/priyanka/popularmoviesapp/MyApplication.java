package com.shreeniclix.priyanka.popularmoviesapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by priyanka on 8/28/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}