package com.example.broker.wallet.error;

import com.example.broker.api.RestApiResponse;

public record CustomError(
        int status,
        String error,
        String message
) implements RestApiResponse {
}
