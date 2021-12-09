package com.mastercard.consumerreferenceapp.util;

import android.content.SharedPreferences;

public class SharePreferenceManager {
    private static SharePreferenceManager sharePreferenceManager = new SharePreferenceManager();
    public static String API_ENDPOINT_KEY = "api_endpoint_key";
    private SharedPreferences sharedPreferences;
    private SharePreferenceManager() {}

    public static SharePreferenceManager getInstance() {
        return sharePreferenceManager;
    }

    public void init(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    public String read(String key, String defValue) {
        return this.sharedPreferences.getString(key, defValue);
    }

    public void write(String key, String value) {
        this.sharedPreferences.edit().putString(key, value).commit();
    }
}
