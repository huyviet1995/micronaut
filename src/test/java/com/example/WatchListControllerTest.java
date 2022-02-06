package com.example;

import com.example.broker.WatchListController;
import com.example.broker.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spockframework.util.Assert;

import java.util.UUID;

@MicronautTest
public class WatchListControllerTest {
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject EmbeddedApplication application;

    @Inject @Client("/account") HttpClient client;

    @Inject InMemoryAccountStore store;

    @Test
    void returnsEmptyWatchListForAccount() {
        final WatchList result = client.toBlocking().retrieve("/watchlist", WatchList.class);
        Assertions.assertTrue(result.getSymbols().isEmpty());
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }
}
