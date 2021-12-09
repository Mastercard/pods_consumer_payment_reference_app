package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;

import com.mastercard.consumerreferenceapp.model.ApiError;
import com.mastercard.consumerreferenceapp.model.ApiErrorMessage;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.model.MastercardUrl;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashboardViewModelTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @InjectMocks
    public DashboardViewModel dashboardViewModel;

    @Mock
    ApiRepository apiRepository;

    @Test
    public void navigate() {
        Observer observer = mock(Observer.class);
        dashboardViewModel.navDirection.observeForever(observer);
        NavDirections navDirections = mock(NavDirections.class);
        dashboardViewModel.navigate(navDirections);
        verify(observer).onChanged(eq(navDirections));
    }

    @Test
    public void navigateBack() {
        Observer observer = mock(Observer.class);
        dashboardViewModel.popBackStack.observeForever(observer);
        dashboardViewModel.navigateBack();
        verify(observer).onChanged(eq(true));
    }

    @Test
    public void makePayment() {
        Contract contract = mock(Contract.class);
        when(contract.getContractId()).thenReturn("contractId");
        dashboardViewModel.setContract(contract);
        ArgumentCaptor<Consumer<MastercardUrl>> successCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<ApiError>> errorCaptor = ArgumentCaptor.forClass(Consumer.class);
        dashboardViewModel.makePayment();
        verify(apiRepository).getPaymentUrl(eq(contract.getContractId()), successCaptor.capture(), errorCaptor.capture());
        // Test Success Consumer
        Consumer successConsumer = successCaptor.getValue();
        MastercardUrl mastercardUrl = mock(MastercardUrl.class);
        when(mastercardUrl.getUrl()).thenReturn("/testUrl");
        when(mastercardUrl.getAccessToken()).thenReturn("token");
        Observer<NavDirections> navDirectionsObserver = mock(Observer.class);
        dashboardViewModel.navDirection.observeForever(navDirectionsObserver);
        successConsumer.accept(mastercardUrl);
        verify(navDirectionsObserver).onChanged(any(NavDirections.class));

        // Test Error Consumer
        Consumer<ApiError> errorConsumer = errorCaptor.getValue();
        Observer errorMessageVisibilityObserver = mock(Observer.class);
        dashboardViewModel.errorMessageVisibility.observeForever(errorMessageVisibilityObserver);
        Observer errorMessageObserver = mock(Observer.class);
        dashboardViewModel.errorMessage.observeForever(errorMessageObserver);
        errorConsumer.accept(new ApiError(ApiErrorMessage.GENERIC_ERROR, "error description"));
        verify(errorMessageObserver).onChanged(eq(ApiErrorMessage.GENERIC_ERROR.getValue()));
        verify(errorMessageVisibilityObserver).onChanged(eq(true));
        Observer enableManageCardBtnObserver = mock(Observer.class);
        Observer enablePaymentBtnObserver = mock(Observer.class);
        dashboardViewModel.enablePaymentBtn.observeForever(enablePaymentBtnObserver);
        dashboardViewModel.enableManageCardBtn.observeForever(enableManageCardBtnObserver);
        verify(enablePaymentBtnObserver).onChanged(eq(true));
        verify(enableManageCardBtnObserver).onChanged(eq(true));
        testDisplayError(ApiErrorMessage.GENERIC_ERROR.getValue());
    }

    @Test
    public void addCard() {
        Contract contract = mock(Contract.class);
        when(contract.getContractId()).thenReturn("contractId");
        dashboardViewModel.setContract(contract);
        ArgumentCaptor<Consumer<MastercardUrl>> successCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<ApiError>> errorCaptor = ArgumentCaptor.forClass(Consumer.class);
        dashboardViewModel.addCard();
        verify(apiRepository).getManageCardUrl(eq(contract.getContractId()), successCaptor.capture(), errorCaptor.capture());
        // Test Success Consumer
        Consumer successConsumer = successCaptor.getValue();
        MastercardUrl mastercardUrl = mock(MastercardUrl.class);
        when(mastercardUrl.getUrl()).thenReturn("/testUrl");
        when(mastercardUrl.getAccessToken()).thenReturn("token");
        Observer<NavDirections> navDirectionsObserver = mock(Observer.class);
        dashboardViewModel.navDirection.observeForever(navDirectionsObserver);
        successConsumer.accept(mastercardUrl);
        verify(navDirectionsObserver).onChanged(any(NavDirections.class));

        // Test Error Consumer
        Consumer<ApiError> errorConsumer = errorCaptor.getValue();
        Observer errorMessageVisibilityObserver = mock(Observer.class);
        dashboardViewModel.errorMessageVisibility.observeForever(errorMessageVisibilityObserver);
        Observer errorMessageObserver = mock(Observer.class);
        dashboardViewModel.errorMessage.observeForever(errorMessageObserver);
        errorConsumer.accept(new ApiError(ApiErrorMessage.GENERIC_ERROR, "error description"));
        verify(errorMessageObserver).onChanged(eq(ApiErrorMessage.GENERIC_ERROR.getValue()));
        verify(errorMessageVisibilityObserver).onChanged(eq(true));
        Observer enableManageCardBtnObserver = mock(Observer.class);
        Observer enablePaymentBtnObserver = mock(Observer.class);
        dashboardViewModel.enablePaymentBtn.observeForever(enablePaymentBtnObserver);
        dashboardViewModel.enableManageCardBtn.observeForever(enableManageCardBtnObserver);
        verify(enablePaymentBtnObserver).onChanged(eq(true));
        verify(enableManageCardBtnObserver).onChanged(eq(true));
        testDisplayError(ApiErrorMessage.GENERIC_ERROR.getValue());
    }

    private void testDisplayError(String messageToVerify) {
        Observer errorMessageVisibilityObserver = mock(Observer.class);
        dashboardViewModel.errorMessageVisibility.observeForever(errorMessageVisibilityObserver);
        Observer errorMessageObserver = mock(Observer.class);
        dashboardViewModel.errorMessage.observeForever(errorMessageObserver);
        verify(errorMessageVisibilityObserver).onChanged(eq(true));
        verify(errorMessageObserver).onChanged(eq(messageToVerify));
    }

    @Test
    public void enableLightBoxButton() {
        Observer enableManageCardBtnObserver = mock(Observer.class);
        Observer enablePaymentBtnObserver = mock(Observer.class);
        dashboardViewModel.enablePaymentBtn.observeForever(enablePaymentBtnObserver);
        dashboardViewModel.enableManageCardBtn.observeForever(enableManageCardBtnObserver);
        dashboardViewModel.enableLightBoxButton();
        verify(enablePaymentBtnObserver).onChanged(eq(true));
        verify(enableManageCardBtnObserver).onChanged(eq(true));
    }

    @Test
    public void disableLightBoxButton() {
        Observer enableManageCardBtnObserver = mock(Observer.class);
        Observer enablePaymentBtnObserver = mock(Observer.class);
        dashboardViewModel.enablePaymentBtn.observeForever(enablePaymentBtnObserver);
        dashboardViewModel.enableManageCardBtn.observeForever(enableManageCardBtnObserver);
        dashboardViewModel.disableLightBoxButton();
        verify(enablePaymentBtnObserver).onChanged(eq(false));
        verify(enableManageCardBtnObserver).onChanged(eq(false));
    }

    @Test
    public void resetViewModel() {
        Observer enablePaymentBtnObserver = mock(Observer.class);
        Observer enableManageCardBtnObserver = mock(Observer.class);
        Observer errorMessageVisibilityObserver = mock(Observer.class);
        dashboardViewModel.enableManageCardBtn.observeForever(enableManageCardBtnObserver);
        dashboardViewModel.enablePaymentBtn.observeForever(enablePaymentBtnObserver);
        dashboardViewModel.errorMessageVisibility.observeForever(errorMessageVisibilityObserver);
        dashboardViewModel.resetViewModel();
        verify(enableManageCardBtnObserver).onChanged(eq(true));
        verify(errorMessageVisibilityObserver).onChanged(eq(false));
        verify(enablePaymentBtnObserver).onChanged(eq(true));
    }

    @Test
    public void getAndSetContract() {
        Contract contract = mock(Contract.class);
        dashboardViewModel.setContract(contract);
        Assert.assertEquals(dashboardViewModel.getContract(), contract);
    }
}