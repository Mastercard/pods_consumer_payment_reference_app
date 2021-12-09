package com.mastercard.consumerreferenceapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Contract implements Parcelable {
    private final String contractId;

    private final String deviceName;

    private final String shopId;

    private final String customerName;

    private final String tenure;

    private final String tenureUnit;

    private final String tenureBalance;

    private final String downPayment;

    private final String balanceDue;

    private final String currency;

    private final String paymentDueDate;

    protected Contract(Parcel in) {
        contractId = in.readString();
        deviceName = in.readString();
        shopId = in.readString();
        customerName = in.readString();
        tenure = in.readString();
        tenureUnit = in.readString();
        tenureBalance = in.readString();
        downPayment = in.readString();
        balanceDue = in.readString();
        currency = in.readString();
        paymentDueDate = in.readString();
    }

    public static final Creator<Contract> CREATOR = new Creator<Contract>() {
        @Override
        public Contract createFromParcel(Parcel in) {
            return new Contract(in);
        }

        @Override
        public Contract[] newArray(int size) {
            return new Contract[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contractId);
        dest.writeString(deviceName);
        dest.writeString(shopId);
        dest.writeString(customerName);
        dest.writeString(tenure);
        dest.writeString(tenureUnit);
        dest.writeString(tenureBalance);
        dest.writeString(downPayment);
        dest.writeString(balanceDue);
        dest.writeString(currency);
        dest.writeString(paymentDueDate);
    }
}