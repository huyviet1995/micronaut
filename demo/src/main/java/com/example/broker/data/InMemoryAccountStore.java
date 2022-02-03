package com.example.broker.data;

import com.example.broker.wallet.Wallet;
import com.example.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import javax.swing.text.html.Option;
import java.util.*;

@Singleton
public class InMemoryAccountStore {
    private final HashMap<UUID, WatchList> watchListsPerAccount = new HashMap<>();
    private final HashMap<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();
    public static final UUID ACCOUNT_ID = UUID.randomUUID();

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
}
