package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.mastercard.consumerreferenceapp.repository.ApiRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ViewModelFactoryTest {
    @InjectMocks
    ViewModelFactory viewModelFactory;

    @Mock
    ApiRepository apiRepository;

    @Test
    public void testCreateNewViewModel() {
        ViewModel viewModel = viewModelFactory.create(MainViewModel.class);
        Assert.assertTrue(viewModel != null);
        Assert.assertTrue(viewModel instanceof MainViewModel );
    }
}
