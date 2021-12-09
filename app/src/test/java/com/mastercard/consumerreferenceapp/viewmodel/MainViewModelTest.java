package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;

import com.mastercard.consumerreferenceapp.fragment.MainFragmentDirections;
import com.mastercard.consumerreferenceapp.model.ApiError;
import com.mastercard.consumerreferenceapp.model.ApiErrorMessage;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @InjectMocks
    public MainViewModel mainViewModel;

    @Before
    public void setup() {
        mainViewModel.apiRepository = mock(ApiRepository.class);
    }

    @Test
    public void navigate() {
        Observer observer = mock(Observer.class);
        mainViewModel.navDirection.observeForever(observer);
        NavDirections navDirections = mock(NavDirections.class);
        mainViewModel.navigate(navDirections);
        verify(observer).onChanged(eq(navDirections));
    }

    @Test
    public void navigateBack() {
        Observer observer = mock(Observer.class);
        mainViewModel.popBackStack.observeForever(observer);
        mainViewModel.navigateBack();
        verify(observer).onChanged(eq(true));
    }

    @Test
    public void configAPIEndpoint() {
        Observer observer = mock(Observer.class);
        mainViewModel.isConfigDialogShown.setValue(false);
        mainViewModel.configureApiEndPoint.observeForever(observer);
        mainViewModel.configAPIEndpoint();
        verify(observer).onChanged(eq(true));
    }

    @Test
    public void configAPIEndpoint_DoesNotPostTrueValueAsConfigureDialogIsShowing() {
        Observer observer = mock(Observer.class);
        mainViewModel.isConfigDialogShown.setValue(true);
        mainViewModel.configureApiEndPoint.setValue(false);
        mainViewModel.configureApiEndPoint.observeForever(observer);
        mainViewModel.configAPIEndpoint();
        verify(observer).onChanged(eq(!true));
    }

    @Test
    public void getContract() {
        ArgumentCaptor<Consumer> successCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer> errorCaptor = ArgumentCaptor.forClass(Consumer.class);
        mainViewModel.getContract();
        verify(mainViewModel.apiRepository).getContract(successCaptor.capture(), errorCaptor.capture());
        // Success Consumer Test
        Consumer<Contract> successConsumer = successCaptor.getValue();
        Contract contract = mock(Contract.class);
        successConsumer.accept(contract);
        Observer<NavDirections> navDirectionsObserver = mock(Observer.class);
        mainViewModel.navDirection.observeForever(navDirectionsObserver);
        verify(navDirectionsObserver).onChanged(eq(MainFragmentDirections.actionMainFragmentToDashboardFragment(contract)));
        // Error Consumer Test
        Consumer<ApiError> errorConsumer = errorCaptor.getValue();
        Observer errorMessageVisibilityObserver = mock(Observer.class);
        mainViewModel.errorMessageVisibility.observeForever(errorMessageVisibilityObserver);
        Observer errorMessageObserver = mock(Observer.class);
        mainViewModel.errorMessage.observeForever(errorMessageObserver);
        errorConsumer.accept(new ApiError(ApiErrorMessage.GENERIC_ERROR, "error description"));
        verify(errorMessageObserver).onChanged(eq(ApiErrorMessage.GENERIC_ERROR.getValue()));
        verify(errorMessageVisibilityObserver).onChanged(eq(true));
    }

    @Test
    public void resetViewModel() {
        Observer errorMessageVisibilityObserver = mock(Observer.class);
        mainViewModel.errorMessageVisibility.observeForever(errorMessageVisibilityObserver);
        Observer configureApiEndPointObserver = mock(Observer.class);
        mainViewModel.configureApiEndPoint.observeForever(configureApiEndPointObserver);
        mainViewModel.resetViewModel();
        verify(errorMessageVisibilityObserver).onChanged(eq(false));
        // twice because the livedata is posted once when it's initialized in the mainViewModel
        verify(configureApiEndPointObserver, times(2)).onChanged(eq(false));
    }
}