package com.mastercard.consumerreferenceapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.model.WebViewData;
import com.mastercard.consumerreferenceapp.model.WebViewType;
import com.mastercard.consumerreferenceapp.navigation.NavigationTestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DashboardFragmentTest extends BaseFragmentTest {
    TestNavHostController testNavHostController;

    @Before
    public void setUp() throws Exception {
        super.setup();
        Bundle fragmentArgs = new Bundle();
        Contract contract = new Contract(
                "contractId",
                "Samsung Phone",
                "Parther Shop",
                "Partner's Customer",
                "10",
                "month",
                "5",
                "50.00",
                "450.00",
                "NGN",
                "2023-02-27T14:19:35+08:00");
        fragmentArgs.putParcelable("contract", contract);
        testNavHostController = NavigationTestHelper.navigationTestSetup(DashboardFragment.class, fragmentArgs, R.id.dashboardFragment);

    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    private String getToken() {
        return "token";
    }

    private String getMasterCardUrlResponseBody() {
        return "{\n" +
                "    \"url\": \"url\",\n" +
                "    \"accessToken\": " + getToken() + "\n" +
                "}";
    }

    @Test
    public void dashboardFragmentTest() {
        onView(withId(R.id.payNowBtn)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.manageCardBtn)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void manageCard() throws InterruptedException {
        onView(withId(R.id.manageCardBtn)).check(matches(isCompletelyDisplayed()));
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(200);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        onView(withId(R.id.manageCardBtn)).perform(click());
        mockWebServer.takeRequest(1000, TimeUnit.SECONDS);
        Assert.assertEquals(testNavHostController.getCurrentDestination().getId(), R.id.webViewFragment);
    }

    @Test
    public void payNow() throws InterruptedException {
        onView(withId(R.id.payNowBtn)).check(matches(isCompletelyDisplayed()));
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(200);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        onView(withId(R.id.payNowBtn)).perform(click());
        mockWebServer.takeRequest(1000, TimeUnit.SECONDS);
        Assert.assertEquals(testNavHostController.getCurrentDestination().getId(), R.id.webViewFragment);
    }
}