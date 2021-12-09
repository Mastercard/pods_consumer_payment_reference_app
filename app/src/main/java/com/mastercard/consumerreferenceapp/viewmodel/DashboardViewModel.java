package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.fragment.DashboardFragmentDirections;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.model.WebViewData;
import com.mastercard.consumerreferenceapp.model.WebViewType;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;

public class DashboardViewModel extends BaseViewModel {
    public MutableLiveData<String> greeting = new MutableLiveData<>();
    public MutableLiveData<String> nextRepaymentAmountHeader = new MutableLiveData<>();
    public MutableLiveData<String> nextRepaymentDays = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorMessageVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> enablePaymentBtn = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableManageCardBtn = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLightBoxDialogShown = new MutableLiveData<>();

    private Contract contract;

    public DashboardViewModel(ApiRepository apiRepository) {
        super(apiRepository);
    }

    public void makePayment() {
        disableLightBoxButton();
        apiRepository.getPaymentUrl(contract.getContractId(), paymentUrl -> {
            WebViewData webViewData = new WebViewData(WebViewType.MAKE_PAYMENT, paymentUrl.getUrl(), paymentUrl.getAccessToken(), isLightBoxDialogShown, paymentUrl.getTokenType(), paymentUrl.getExpiresIn());
            navigate(DashboardFragmentDirections.actionDashboardFragmentToWebViewFragment(webViewData));
        }, apiError -> {
            enableLightBoxButton();
            displayError(apiError.getErrorMessage().getValue());
        });
    }

    public void addCard() {
        disableLightBoxButton();
        apiRepository.getManageCardUrl(contract.getContractId(), addCardUrl -> {
            WebViewData webViewData = new WebViewData(WebViewType.MANAGE_CARD, addCardUrl.getUrl(), addCardUrl.getAccessToken(), isLightBoxDialogShown, addCardUrl.getTokenType(), addCardUrl.getExpiresIn());
            navigate(DashboardFragmentDirections.actionDashboardFragmentToWebViewFragment(webViewData));
        }, apiError -> {
            enableLightBoxButton();
            displayError(apiError.getErrorMessage().getValue());
        });
    }

    public void enableLightBoxButton() {
        enableManageCardBtn.postValue(true);
        enablePaymentBtn.postValue(true);
    }

    public void disableLightBoxButton() {
        enableManageCardBtn.postValue(false);
        enablePaymentBtn.postValue(false);
    }

    private void displayError(String message) {
        errorMessageVisibility.postValue(true);
        errorMessage.postValue(message);
    }

    public void resetViewModel() {
        enablePaymentBtn.postValue(true);
        enableManageCardBtn.postValue(true);
        errorMessageVisibility.postValue(false);
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
