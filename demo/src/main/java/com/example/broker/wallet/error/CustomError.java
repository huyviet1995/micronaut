package com.example.broker.wallet;

public record CustomError(
        int status,
        String error,
        String message
) {
}
