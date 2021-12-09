package com.mastercard.consumerreferenceapp.model;

public enum WebViewType {
    MANAGE_CARD("Manage Card"),
    MAKE_PAYMENT("Payment");

    private String value;

    WebViewType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
