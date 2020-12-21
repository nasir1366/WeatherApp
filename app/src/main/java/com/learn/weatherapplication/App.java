package com.learn.weatherapplication;

import android.app.Application;

import com.learn.weatherapplication.data.AssetsDatabaseHelper;
//if could not find database ,copy it from assets folder
public class App extends Application {
   public static final String API_KEY = "8d167e13c5cf26c8acd7c09eb89e1e0b";

    @Override
    public void onCreate() {
        super.onCreate();
        new AssetsDatabaseHelper(getApplicationContext()).checkDb();
    }
}
