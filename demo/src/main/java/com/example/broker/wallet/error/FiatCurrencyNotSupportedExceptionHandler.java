package com.example.broker.wallet.error;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import static com.example.broker.wallet.WalletController.SUPPORTED_FIAT_CURRENCY;

@Singleton
@Requires(classes = {FiatCurrencyNotSupportedException.class, ExceptionHandler.class })
public class FiatCurrencyNotSupportedExceptionHandler implements ExceptionHandler<FiatCurrencyNotSupportedException, HttpResponse<CustomError>> {
    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, FiatCurrencyNotSupportedException exception) {
        return HttpResponse.badRequest(
                new CustomError(
                        HttpStatus.BAD_REQUEST.getCode(),
                        "UNSUPPORTED FIAT CURRENCY",
                        String.format("Only %s are supported as currency", SUPPORTED_FIAT_CURRENCY)
                )
        );
    }
}
