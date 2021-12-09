package com.mastercard.consumerreferenceapp.repository;

import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.model.MastercardUrl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ReferenceApi {
    @GET("ping")
    Call<String> healthCheck();

    @GET("contract")
    Call<Contract> getContract();

    @GET("payment")
    Call<MastercardUrl> getPaymentUrl(@Query("contract_id") String contractId);

    @GET("manage_card")
    Call<MastercardUrl> getManageCardUrl(@Query("contract_id") String contractId);
}
