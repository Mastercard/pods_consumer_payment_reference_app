package com.mastercard.consumerreferenceapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MastercardUrl {
    private final String url;
    private final String accessToken;
    private final String tokenType;
    private final String expiresIn;
}
