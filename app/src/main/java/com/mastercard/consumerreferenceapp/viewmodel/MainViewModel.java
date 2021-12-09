package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.mastercard.consumerreferenceapp.fragment.MainFragmentDirections;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;

import timber.log.Timber;


public class MainViewModel extends BaseViewModel {
    public MutableLiveData<String> versionNumber = new MutableLiveData<>();
    public MutableLiveData<Boolean> configureApiEndPoint = new MutableLiveData<>(false);
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorMessageVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConfigDialogShown = new MutableLiveData<>(false);

    public MainViewModel(ApiRepository apiRepository) {
        super(apiRepository);
    }

    public void configAPIEndpoint() {
        if(!isConfigDialogShown.getValue()) {
            configureApiEndPoint.postValue(true);
        }
    }

    public void getContract() {
        apiRepository.getContract(contract -> {
            Timber.i(contract.toString());
            navigate(MainFragmentDirections.actionMainFragmentToDashboardFragment(contract));
        }, apiError -> {
            errorMessageVisibility.postValue(true);
            errorMessage.postValue(apiError.getErrorMessage().getValue());
        });
    }

    public void resetViewModel() {
        errorMessageVisibility.postValue(false);
        configureApiEndPoint.postValue(false);
    }

}
