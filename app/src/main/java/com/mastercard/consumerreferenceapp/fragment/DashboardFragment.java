package com.mastercard.consumerreferenceapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.mastercard.consumerreferenceapp.BuildConfig;
import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.databinding.FragmentDashboardBinding;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.repository.ApiRepository;
import com.mastercard.consumerreferenceapp.util.SharePreferenceManager;
import com.mastercard.consumerreferenceapp.viewmodel.DashboardViewModel;
import com.mastercard.consumerreferenceapp.viewmodel.ViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashboardFragment extends BaseFragment {
    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private ApiRepository apiRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiRepository = ApiRepository.getInstance(SharePreferenceManager.getInstance().read(SharePreferenceManager.API_ENDPOINT_KEY, "http://localhost:8081/"));
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(apiRepository)).get(DashboardViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        super.baseViewModel = viewModel;
        Contract contract = DashboardFragmentArgs.fromBundle(getArguments()).getContract();
        if (contract == null) {
            navigateBack();
        } else {
            this.setUpUI(contract);
            viewModel.setContract(contract);
            viewModel.enableLightBoxButton();
            // To prevent multiple button click to show lightbox dialog, the button is disabled when user click and only enabled once the light box is shown.
            viewModel.isLightBoxDialogShown.observe(getViewLifecycleOwner(), isPaymentDialogShown -> {
                if (isPaymentDialogShown) {
                    viewModel.enableLightBoxButton();
                }
            });
        }
        return binding.getRoot();
    }

    private void setUpUI(Contract contract) {
        viewModel.greeting.postValue("Good Morning,\n" + contract.getCustomerName() + "!");
        String nextDueDate = contract.getPaymentDueDate().split("T")[0];
        viewModel.nextRepaymentAmountHeader.postValue(String.format("A next repayment amount of %s %s is due on %s.", contract.getCurrency(), contract.getBalanceDue(), nextDueDate));
        binding.setDueDateDetail(String.format("Your next due date is <b>%s</b>", nextDueDate));
        viewModel.nextRepaymentDays.postValue(Integer.toString(this.getDayLeftTillPayment(nextDueDate)));
        binding.dashboardCancelBtn.setOnClickListener(v -> navigateBack());
    }

    private int getDayLeftTillPayment(String nextDueDate) {
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
            Calendar dueDate = toCalendar(curFormater.parse(nextDueDate).getTime());
            Calendar currentDate = toCalendar(System.currentTimeMillis());
            long diff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
            if (diff <= 0) {
                binding.progressView.setProgressColorRes(R.color.colorCircleRed);
                binding.progressView.setTotalColorRes(R.color.colorCircleDarkRed);
                binding.progressShadow.setImageResource(R.drawable.shared_progress_shadow_red);
                return 0;
            }
            return (int) (Math.abs(diff) / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 10;
    }

    private Calendar toCalendar(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.resetViewModel();
        viewModel.isLightBoxDialogShown.removeObservers(getViewLifecycleOwner());
    }
}
