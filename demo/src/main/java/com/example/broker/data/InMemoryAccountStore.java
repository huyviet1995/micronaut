package com.example.broker.data;

import com.example.broker.wallet.DepositFiatMoney;
import com.example.broker.wallet.Wallet;
import com.example.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.*;

@Singleton
public class InMemoryAccountStore {
    private final HashMap<UUID, WatchList> watchListsPerAccount = new HashMap<>();
    private final HashMap<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();
    public static final UUID ACCOUNT_ID = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

    public WatchList getWatchList(final UUID accountId) {
        return watchListsPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListsPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchList(final UUID accountId) {
        watchListsPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletsPerAccount.get(accountId))
                .orElse(new HashMap<UUID, Wallet>())
                .values();
    }

    public Wallet depositToWallet(DepositFiatMoney deposit) {
        final var wallets = Optional.ofNullable(
                walletsPerAccount.get(deposit.accountId())
        ).orElse(new HashMap<>());

        var oldWallet = Optional.ofNullable(wallets.get(deposit.walletId())).orElse(new Wallet(ACCOUNT_ID, deposit.walletId(), deposit.symbol(), BigDecimal.ZERO, BigDecimal.ZERO));
        var newWallet = oldWallet.addAvailable(deposit.amount());
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }
}
