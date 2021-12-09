package com.mastercard.consumerreferenceapp.fragment;

import androidx.test.rule.ActivityTestRule;

import com.mastercard.consumerreferenceapp.MainActivity;
import com.mastercard.consumerreferenceapp.repository.ApiClient;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;

public class BaseFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    public ApiRepository apiRepository;

    public String TEST_URL = "/test/";

    public MockWebServer mockWebServer = new MockWebServer();

    public void setup() throws IOException, IllegalAccessException, InstantiationException {
        apiRepository = ApiRepository.getInstance("http://0.0.0.0");
        mockWebServer.start();
        HttpUrl url = mockWebServer.url(TEST_URL);
        apiRepository.updateApiEndpoint(url);
    }

    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
