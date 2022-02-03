package com.example.broker.wallet;

import com.example.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositFiatMoney(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal amount
) {

}
