package com.mastercard.consumerreferenceapp.repository;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;

import static org.junit.Assert.*;

public class ApiClientTest {

    @Test
    public void getApiClient() {
        ApiClient apiClient = ApiClient.getApiClient();
        Assert.assertNotNull(apiClient);
    }

    @Test
    public void getRetrofitInstance() {
        ApiClient apiClient = ApiClient.getApiClient();
        String testUrl = "http://0.0.0.0/testing/";
        Retrofit retrofit = apiClient.getRetrofitInstance(testUrl);
        Assert.assertEquals(testUrl, retrofit.baseUrl().toString());
    }

    @Test
    public void getRetrofitInstance_withHttpUrl() {
        ApiClient apiClient = ApiClient.getApiClient();
        HttpUrl testUrl = HttpUrl.parse("http://0.0.0.0/testing/");
        Retrofit retrofit = apiClient.getRetrofitInstance(testUrl);
        Assert.assertEquals(testUrl.toString(), retrofit.baseUrl().toString());
    }
}