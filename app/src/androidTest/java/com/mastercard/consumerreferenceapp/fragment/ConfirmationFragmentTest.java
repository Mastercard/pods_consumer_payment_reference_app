package com.mastercard.consumerreferenceapp.fragment;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mastercard.consumerreferenceapp.R;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ConfirmationFragmentTest extends BaseFragmentTest {
    TestNavHostController testNavHostController;

    @Before
    public void setUp() throws Exception {
        super.setup();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void testConfirmationFragment_PaymentSuccess() throws IllegalAccessException, InstantiationException {
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putBoolean("paymentSuccess", true);
        testNavHostController = NavigationTestHelper.navigationTestSetup(ConfirmationFragment.class, fragmentArgs, R.id.confirmationFragment);
        onView(withId(R.id.confirm_header)).check(matches(withText(R.string.confirmation_header_success)));
        onView(withId(R.id.confirm_description)).check(matches(withText(R.string.confirmation_description_success)));
    }

    @Test
    public void testConfirmationFragment_PaymentDeclined() throws IllegalAccessException, InstantiationException {
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putBoolean("paymentSuccess", false);
        testNavHostController = NavigationTestHelper.navigationTestSetup(ConfirmationFragment.class, fragmentArgs, R.id.confirmationFragment);
        onView(withId(R.id.confirm_header)).check(matches(withText(R.string.confirmation_header_declined)));
        onView(withId(R.id.confirm_description)).check(matches(withText(R.string.confirmation_description_declined)));
    }
}