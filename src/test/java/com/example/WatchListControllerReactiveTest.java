package com.example;

import com.example.broker.JWTWatchListClient;
import com.example.broker.WatchListControllerReactive;
import com.example.broker.persistence.model.Symbol;
import com.example.broker.persistence.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@MicronautTest
public class WatchListControllerReactiveTest {
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final UUID TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID;

    @Inject EmbeddedApplication application;

    @Inject @Client("/")
    JWTWatchListClient client;

    @Inject InMemoryAccountStore store;
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactive.class);
    public static final String ACCOUNT_WATCHLIST = "/account/watchlist";

    @Test
    void returnsEmptyWatchListForAccount() {
        final Single<WatchList> result = client.retrieveWatchList(getAuthorizationHeader()).singleOrError();
        Assertions.assertTrue(result.blockingGet().getSymbols().isEmpty());
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForAccount() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        var result = client.retrieveWatchList(getAuthorizationHeader()).singleOrError().blockingGet();
        Assertions.assertEquals(3, result.getSymbols().size());
        Assertions.assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void returnsWatchListForAccountAsSingle() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final WatchList result = client.retrieveWatchListAsSingle(getAuthorizationHeader()).blockingGet();
        Assertions.assertEquals(3, result.getSymbols().size());
        Assertions.assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void canUpdateWatchListForAccount() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        final HttpResponse<Object> response = client.updateWatchList(getAuthorizationHeader(), watchList);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }


    @Test
    void canDeleteWatchlistForAccount() {
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final HttpResponse<Object> response = client.deleteWatchList(getAuthorizationHeader(), TEST_ACCOUNT_ID);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    private String getAuthorizationHeader() {
        return "Bearer " + givenMyUserLoggedIn().getAccessToken();
    }

    private BearerAccessRefreshToken givenMyUserLoggedIn() {
        return client.login(new UsernamePasswordCredentials("my-user", "secret"));
    }
}
