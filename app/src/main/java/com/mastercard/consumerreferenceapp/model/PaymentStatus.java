package com.mastercard.consumerreferenceapp.model;

public enum PaymentStatus {
    SUCCESS("success"),
    FAIL("fail");

    private String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
