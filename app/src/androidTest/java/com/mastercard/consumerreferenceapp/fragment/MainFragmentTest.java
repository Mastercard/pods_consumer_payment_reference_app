package com.mastercard.consumerreferenceapp.fragment;

import androidx.navigation.testing.TestNavHostController;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.navigation.NavigationTestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainFragmentTest extends BaseFragmentTest {
    TestNavHostController testNavHostController;

    @Before
    public void setup() throws IOException, IllegalAccessException, InstantiationException {
        super.setup();
        testNavHostController = NavigationTestHelper.navigationTestSetup(MainFragment.class, null, R.id.mainFragment);
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    private String getContractId() {
        return "f730fb62-c2fb-4764-846a-31c9eb14efbe";
    }


    private String getContractResponseBody() {
        return "{\n" +
                "    \"contractId\": " + getContractId() + ",\n" +
                "    \"deviceName\": \"Samsung Phone\",\n" +
                "    \"shopId\": \"Parther Shop\",\n" +
                "    \"customerName\": \"Partner's Customer\",\n" +
                "    \"tenure\": \"10\",\n" +
                "    \"tenureUnit\": \"month\",\n" +
                "    \"tenureBalance\": \"5\",\n" +
                "    \"downPayment\": \"50.00\",\n" +
                "    \"balanceDue\": \"450.00\",\n" +
                "    \"currency\": \"UGX\",\n" +
                "    \"paymentDueDate\": \"2021-02-24T14:05:41+08:00\"\n" +
                "}";
    }

    @Test
    public void mainFragement() {
        onView(withId(R.id.main_greeting)).check(matches(withText(R.string.main_greeting)));
        onView(withId(R.id.main_description)).check(matches(withText(R.string.main_description)));
    }

    @Test
    public void testSettingDialogDisplay() {
        onView(withId(R.id.settingBtn)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.settingBtn)).perform(click());
        onView(withId(R.id.configDialogHeader)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.configUrl)).perform(typeText("http://127.0.0.1:8080/"));
        onView(withId(R.id.configSubmitBtn)).perform(click());
        onView(withId(R.id.configDialogHeader)).check(doesNotExist());
    }

    @Test
    public void testStartBtnClick() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(200);
        mockResponse.setBody(getContractResponseBody());
        mockWebServer.enqueue(mockResponse);
        onView(withId(R.id.startBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.startBtn)).perform(click());
        mockWebServer.takeRequest(10, TimeUnit.SECONDS);
        Assert.assertEquals(testNavHostController.getCurrentDestination().getId(), R.id.dashboardFragment);
    }
}