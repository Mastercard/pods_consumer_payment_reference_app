package com.mastercard.consumerreferenceapp.navigation;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;

import com.mastercard.consumerreferenceapp.R;


public class NavigationTestHelper {
    public static  <F extends Fragment> TestNavHostController navigationTestSetup(Class<F> fragmentClass, Bundle fragmentArgs, @IdRes int currentDestinationId) throws InstantiationException, IllegalAccessException {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);
        if(fragmentArgs != null) {
            navController.setCurrentDestination(currentDestinationId, fragmentArgs);
        } else {
            navController.setCurrentDestination(currentDestinationId);
        }
        F fragmentInstance = fragmentClass.newInstance();

        // Create a graphical FragmentScenario
        // In addition to returning a new instance of our fragment,
        // get a callback whenever the fragment’s view is created
        // or destroyed so that we can set the NavController
        FragmentScenario scenario = FragmentScenario.launchInContainer(fragmentClass, fragmentArgs, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {

                fragmentInstance.getViewLifecycleOwnerLiveData().observeForever(lifecycleOwner -> {
                    if(lifecycleOwner != null) {
                        Navigation.setViewNavController(fragmentInstance.requireView(), navController);
                    }
                });
                return fragmentInstance;
            }
        });

        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        return navController;
    }
}
