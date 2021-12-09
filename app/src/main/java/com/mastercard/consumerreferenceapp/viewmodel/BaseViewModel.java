package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;

import com.mastercard.consumerreferenceapp.repository.ApiRepository;

public abstract class BaseViewModel extends ViewModel {
    public MutableLiveData<NavDirections> navDirection = new MutableLiveData<>();
    public MutableLiveData<Boolean> popBackStack = new MutableLiveData<>();
    protected ApiRepository apiRepository;

    public BaseViewModel(ApiRepository apiRepository){
        this.apiRepository = apiRepository;
    }

    public void navigate(NavDirections navDirections) {
        navDirection.postValue(navDirections);
    }

    public void navigateBack() {
        popBackStack.postValue(true);
    }
}
