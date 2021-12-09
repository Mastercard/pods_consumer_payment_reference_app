package com.mastercard.consumerreferenceapp.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mastercard.consumerreferenceapp.repository.ApiRepository;

import lombok.NonNull;
import lombok.SneakyThrows;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private ApiRepository apiRepository;

    public ViewModelFactory(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @SneakyThrows
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.getDeclaredConstructor(ApiRepository.class).newInstance(apiRepository);
    }
}

