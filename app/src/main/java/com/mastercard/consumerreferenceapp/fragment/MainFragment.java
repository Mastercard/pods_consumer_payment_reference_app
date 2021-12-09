package com.mastercard.consumerreferenceapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.mastercard.consumerreferenceapp.BuildConfig;
import com.mastercard.consumerreferenceapp.databinding.FragmentMainBinding;
import com.mastercard.consumerreferenceapp.repository.ApiClient;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;
import com.mastercard.consumerreferenceapp.repository.ReferenceApi;
import com.mastercard.consumerreferenceapp.util.SharePreferenceManager;
import com.mastercard.consumerreferenceapp.viewmodel.MainViewModel;
import com.mastercard.consumerreferenceapp.viewmodel.ViewModelFactory;

import lombok.SneakyThrows;

public class MainFragment extends BaseFragment {
    private FragmentMainBinding binding;
    private MainViewModel viewModel;
    private ApiRepository apiRepository;

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiRepository = ApiRepository.getInstance(SharePreferenceManager.getInstance().read(SharePreferenceManager.API_ENDPOINT_KEY, "http://localhost:8081/"));
        viewModel = new ViewModelProvider(this, new ViewModelFactory(apiRepository)).get(MainViewModel.class);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this.getViewLifecycleOwner());
        super.baseViewModel = viewModel;
        viewModel.versionNumber.postValue("v" + getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName);
        ConfigDialogFragment configDialogFragment = ConfigDialogFragment.newInstance(viewModel.isConfigDialogShown, apiEndpoint ->
        {
            SharePreferenceManager.getInstance().write(SharePreferenceManager.API_ENDPOINT_KEY, apiEndpoint);
            try {
                apiRepository.updateApiEndpoint(apiEndpoint);
                binding.mainError.setVisibility(View.GONE);
                binding.startBtn.setEnabled(true);
            } catch (IllegalArgumentException ex) {
                binding.mainError.setVisibility(View.VISIBLE);
                viewModel.errorMessage.postValue(ex.getMessage());
                binding.startBtn.setEnabled(false);
            }
        });
        viewModel.configureApiEndPoint.observe(getViewLifecycleOwner(), configureApiEndpoint -> {
            if (configureApiEndpoint) {
                configDialogFragment.show(getParentFragmentManager(), "dialog");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.resetViewModel();
    }
}
