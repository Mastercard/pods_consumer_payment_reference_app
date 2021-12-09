package com.mastercard.consumerreferenceapp.model;

public enum ApiErrorMessage {
    GENERIC_ERROR("Error. Please check network or input and try again later!"),
    BAD_REQUEST("Invalid Input. Please try again!"),
    SERVER_ERROR("Server Error. Please try again!");

    private String value;

    ApiErrorMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
