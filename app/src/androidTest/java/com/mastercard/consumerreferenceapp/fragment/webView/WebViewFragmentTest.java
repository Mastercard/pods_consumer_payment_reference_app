package com.mastercard.consumerreferenceapp.fragment.webView;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.fragment.BaseFragmentTest;
import com.mastercard.consumerreferenceapp.model.WebViewData;
import com.mastercard.consumerreferenceapp.model.WebViewType;
import com.mastercard.consumerreferenceapp.navigation.NavigationTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WebViewFragmentTest extends BaseFragmentTest {
    TestNavHostController testNavHostController;

    @Before
    public void setUp() throws Exception {
        super.setup();
        Bundle fragmentArgs = new Bundle();
        WebViewData webViewData = new WebViewData(WebViewType.MAKE_PAYMENT, "http://www.google.com", "token", new MutableLiveData<>());
        fragmentArgs.putParcelable("webViewData", webViewData);
        testNavHostController = NavigationTestHelper.navigationTestSetup(WebViewFragment.class, fragmentArgs, R.id.webViewFragment);
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void testWebViewFragmentDisplay() {
        onView(withId(R.id.lbl_title)).check(matches(withText(WebViewType.MAKE_PAYMENT.getValue())));
        onView(withId(R.id.backButton)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.imgClose)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.web_view)).check(matches(isCompletelyDisplayed()));
    }
}