package com.mastercard.consumerreferenceapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.mastercard.consumerreferenceapp.databinding.FragmentDialogConfigBinding;
import com.mastercard.consumerreferenceapp.util.SharePreferenceManager;

import java.util.function.Consumer;

public class ConfigDialogFragment extends DialogFragment {
    private FragmentDialogConfigBinding binding;
    private MutableLiveData<Boolean> isConfigDialogShown;
    Consumer<String> callBack;

    public ConfigDialogFragment(MutableLiveData<Boolean> isConfigDialogShown, Consumer<String> callBack) {
        this.callBack = callBack;
        this.isConfigDialogShown = isConfigDialogShown;

    }

    public static ConfigDialogFragment newInstance(MutableLiveData<Boolean> isConfiguringAPIEndpoint, @NonNull Consumer<String> callBack) {
        ConfigDialogFragment configDialogFragment = new ConfigDialogFragment(isConfiguringAPIEndpoint, callBack);
        return configDialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogConfigBinding.inflate(inflater, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.configUrl.setText(SharePreferenceManager.getInstance().read(SharePreferenceManager.API_ENDPOINT_KEY, ""));
        binding.configSubmitBtn.setOnClickListener(this::onClick);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.isConfigDialogShown.postValue(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.isConfigDialogShown.postValue(false);
    }

    private void onClick(View v) {
        callBack.accept(binding.configUrl.getText().toString());
        dismiss();
    }
}
