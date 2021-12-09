package com.mastercard.consumerreferenceapp;

import android.app.Application;
import android.content.Context;

import com.mastercard.consumerreferenceapp.util.SharePreferenceManager;

import okhttp3.HttpUrl;
import timber.log.Timber;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        String preferenceName = this.getPackageName() + "_preferences";
        SharePreferenceManager sharePreferenceManager = SharePreferenceManager.getInstance();
        sharePreferenceManager.init(getSharedPreferences(preferenceName, Context.MODE_PRIVATE));
        String baseUrl;
        if(HttpUrl.parse(BuildConfig.BASE_URL) == null) {
            baseUrl = "http://localhost:8081/";
        } else {
            baseUrl = BuildConfig.BASE_URL;
        }

        sharePreferenceManager.write(SharePreferenceManager.API_ENDPOINT_KEY, baseUrl);
    }
}
