package com.mastercard.consumerreferenceapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiError {
    private final ApiErrorMessage errorMessage;
    private final String errorDescription;
}
