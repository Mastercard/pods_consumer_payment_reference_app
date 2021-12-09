package com.mastercard.consumerreferenceapp.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final ApiClient API_CLIENT = new ApiClient();
    private Retrofit retrofit;
    private String currentBaseUrl = "";

    private ApiClient() {
    }

    public static ApiClient getApiClient() {
        return API_CLIENT;
    }

    public Retrofit getRetrofitInstance(String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if(HttpUrl.parse(baseUrl) == null) {
            throw new IllegalArgumentException("Illegal Url: " + baseUrl);
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        if(retrofit == null || !currentBaseUrl.equals(baseUrl)) {
            this.retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(httpClient.build()).addConverterFactory(GsonConverterFactory.create(gson)).build();
            this.currentBaseUrl = baseUrl;
        }
        return this.retrofit;
    }

    public Retrofit getRetrofitInstance(HttpUrl baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        if(retrofit == null || !currentBaseUrl.equals(baseUrl.toString())) {
            this.retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(httpClient.build()).addConverterFactory(GsonConverterFactory.create(gson)).build();
            this.currentBaseUrl = baseUrl.toString();
        }
        return this.retrofit;
    }
}
