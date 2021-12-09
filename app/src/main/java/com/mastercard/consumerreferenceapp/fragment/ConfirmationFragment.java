package com.mastercard.consumerreferenceapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.databinding.FragmentConfirmationBinding;

public class ConfirmationFragment extends BaseFragment {
    private FragmentConfirmationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false);
        binding.confirmCancelBtn.setOnClickListener(v -> navigateBack());
        boolean paymentSuccess = ConfirmationFragmentArgs.fromBundle(getArguments()).getPaymentSuccess();
        if (paymentSuccess) {
            binding.confirmHeader.setText(getResources().getString(R.string.confirmation_header_success));
            binding.confirmDescription.setText(getResources().getString(R.string.confirmation_description_success));
            binding.confirmSymbol.setImageResource(R.drawable.success_tick);
        } else {
            binding.confirmHeader.setText(getResources().getString(R.string.confirmation_header_declined));
            binding.confirmDescription.setText(getResources().getString(R.string.confirmation_description_declined));
            binding.confirmSymbol.setImageResource(R.drawable.decline_cross);
        }
        return binding.getRoot();
    }
}
