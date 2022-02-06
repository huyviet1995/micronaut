package com.example;

import com.example.broker.WatchListController;
import com.example.broker.model.Symbol;
import com.example.broker.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spockframework.util.Assert;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Test
    void returnsWatchListForAccount() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final WatchList result = client.toBlocking().retrieve("/watchlist", WatchList.class);
        Assertions.assertEquals(3, result.getSymbols().size());
        Assertions.assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void canUpdateWatchListForAccount() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        final HttpResponse<Object> response = client.toBlocking().exchange(HttpRequest.PUT("/watchlist", watchList));
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    void canDeleteWatchlistForAccount() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final HttpResponse<Object> response = client.toBlocking().exchange(HttpRequest.DELETE("/watchlist/" + TEST_ACCOUNT_ID));
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }
}
