package com.example.broker.wallet;

import com.example.broker.Symbol;
import com.example.broker.api.RestApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked
) implements RestApiResponse {

    public Wallet addAvailable(BigDecimal amountToAdd) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToAdd),
                this.locked
        );
    }

    public Wallet subtractAvailable(BigDecimal amountToRemove) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.subtract(amountToRemove),
                this.locked
        );
    }
}
