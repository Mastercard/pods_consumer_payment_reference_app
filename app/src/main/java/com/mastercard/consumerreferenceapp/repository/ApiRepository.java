package com.mastercard.consumerreferenceapp.repository;

import com.mastercard.consumerreferenceapp.model.ApiError;
import com.mastercard.consumerreferenceapp.model.ApiErrorMessage;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.model.MastercardUrl;

import java.util.function.Consumer;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ApiRepository {
    private static ApiRepository INSTANCE = new ApiRepository();
    private ReferenceApi referenceApi;

    private String currentEndPoint;

    private ApiRepository() {

    }

    public static ApiRepository getInstance(String baseUrl) {
        if(INSTANCE.referenceApi == null) {
            INSTANCE.referenceApi = ApiClient.getApiClient().getRetrofitInstance(baseUrl).create(ReferenceApi.class);
            INSTANCE.currentEndPoint = baseUrl;
        }
        return INSTANCE;
    }

    public String getCurrentEndPoint() {
        return currentEndPoint;
    }

    public void updateApiEndpoint(String apiEndPoint) throws IllegalArgumentException {
        if (!currentEndPoint.equals(apiEndPoint)) {
            this.referenceApi = ApiClient.getApiClient().getRetrofitInstance(apiEndPoint).create(ReferenceApi.class);
            this.currentEndPoint = apiEndPoint;
        }
    }

    public void updateApiEndpoint(HttpUrl apiEndPoint) throws IllegalArgumentException {
        if (!currentEndPoint.equals(apiEndPoint)) {
            this.referenceApi = ApiClient.getApiClient().getRetrofitInstance(apiEndPoint).create(ReferenceApi.class);
            this.currentEndPoint = apiEndPoint.toString();
        }
    }

    public void getContract(Consumer<Contract> successCallBack, Consumer<ApiError> errorCallBack) {
        Call<Contract> call = referenceApi.getContract();
        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                Timber.i(response.toString());
                if (response.isSuccessful()) {
                    successCallBack.accept(response.body());
                } else if (response.code() >= 500) {
                    errorCallBack.accept(new ApiError(ApiErrorMessage.SERVER_ERROR, response.message()));
                } else {
                    errorCallBack.accept(new ApiError(ApiErrorMessage.BAD_REQUEST, response.message()));
                }
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {
                Timber.e(t.getMessage());
                errorCallBack.accept(new ApiError(ApiErrorMessage.GENERIC_ERROR, t.getMessage()));
            }
        });
    }

    public void getPaymentUrl(String contractId, Consumer<MastercardUrl> successCallBack, Consumer<ApiError> errorCallBack) {
        Call<MastercardUrl> call = referenceApi.getPaymentUrl(contractId);
        call.enqueue(new Callback<MastercardUrl>() {
            @Override
            public void onResponse(Call<MastercardUrl> call, Response<MastercardUrl> response) {
                Timber.i(response.toString());
                if (response.isSuccessful()) {
                    successCallBack.accept(response.body());
                } else if (response.code() >= 500) {
                    errorCallBack.accept(new ApiError(ApiErrorMessage.SERVER_ERROR, response.message()));
                } else {
                    errorCallBack.accept(new ApiError(ApiErrorMessage.BAD_REQUEST, response.message()));
                }
            }

            @Override
            public void onFailure(Call<MastercardUrl> call, Throwable t) {
                Timber.e(t.getMessage());
                errorCallBack.accept(new ApiError(ApiErrorMessage.GENERIC_ERROR, t.getMessage()));
            }
        });
    }

    public void getManageCardUrl(String contractId, Consumer<MastercardUrl> successCallBack, Consumer<ApiError> errorCallBack) {
        Call<MastercardUrl> call = referenceApi.getManageCardUrl(contractId);
        call.enqueue(new Callback<MastercardUrl>() {
            @Override
            public void onResponse(Call<MastercardUrl> call, Response<MastercardUrl> response) {
                Timber.i(response.toString());
                if (response.isSuccessful()) {
                    successCallBack.accept(response.body());
                } else if (response.code() >= 500) {
                    errorCallBack.accept(new ApiError(ApiErrorMessage.SERVER_ERROR, response.message()));
                } else {
                    errorCallBack.accept(new ApiError(ApiErrorMessage.BAD_REQUEST, response.message()));
                }
            }

            @Override
            public void onFailure(Call<MastercardUrl> call, Throwable t) {
                Timber.e(t.getMessage());
                errorCallBack.accept(new ApiError(ApiErrorMessage.GENERIC_ERROR, t.getMessage()));
            }
        });
    }
}
