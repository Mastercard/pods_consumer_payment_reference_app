package com.mastercard.consumerreferenceapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.mastercard.consumerreferenceapp.viewmodel.BaseViewModel;

public abstract class BaseFragment extends Fragment {
    protected BaseViewModel baseViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (baseViewModel != null) {
            baseViewModel.popBackStack.setValue(false);
            baseViewModel.navDirection.setValue(null);
            baseViewModel.navDirection.observe(getViewLifecycleOwner(), navDirection -> {
                if (navDirection instanceof NavDirections) {
                    this.navigate(navDirection);
                }
            });
            baseViewModel.popBackStack.observe(getViewLifecycleOwner(), navigateBack -> {
                if (navigateBack == true) {
                    this.navigateBack();
                }
            });
        }
    }

    public void navigateBack() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    public void navigate(NavDirections navDirection) {
        NavHostFragment.findNavController(this).navigate(navDirection);
    }
}
