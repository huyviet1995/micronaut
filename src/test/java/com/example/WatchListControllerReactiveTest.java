package com.example;

import com.example.broker.WatchListControllerReactive;
import com.example.broker.model.Symbol;
import com.example.broker.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.rxjava2.http.client.RxHttpClient;
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

import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest
public class WatchListControllerReactiveTest {
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final UUID TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID;

    @Inject EmbeddedApplication application;

    @Inject @Client("/")
    RxHttpClient client;

    @Inject InMemoryAccountStore store;
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactive.class);
    public static final String ACCOUNT_WATCHLIST = "/account/watchlist";

    private BearerAccessRefreshToken givenMyUserIsLoggedIn() {
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("my-user", "secret");
        var login = HttpRequest.POST("/login", credentials);
        final HttpResponse<BearerAccessRefreshToken> response = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        final BearerAccessRefreshToken token = response.body();
        Assertions.assertNotNull(token);;
        Assertions.assertEquals("my-user", response.body().getUsername());
        LOG.debug("LOGIN Access Token {}", token.getAccessToken(), token.getExpiresIn());
        return token;
    }

    @Test
    void unauthorizedAccessIsForbidden() {
        try {
            client.toBlocking().retrieve(ACCOUNT_WATCHLIST);
            fail("should fail if no exception is thrown");
        } catch (HttpClientResponseException e) {
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        }
    }

    @Test
    void returnsEmptyWatchListForAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();
        var request = HttpRequest.GET("/account/watchlist-reactive")
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());

        final Single<WatchList> result = client.retrieve(request, WatchList.class).singleOrError();
        Assertions.assertTrue(result.blockingGet().getSymbols().isEmpty());
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();
        var request = HttpRequest.GET("/account/watchlist-reactive")
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);
        final WatchList result = client.toBlocking().retrieve(request, WatchList.class);
        Assertions.assertEquals(3, result.getSymbols().size());
        Assertions.assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void returnsWatchListForAccountAsSingle() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();
        var request = HttpRequest.GET("/account/watchlist-reactive/single")
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final Single<WatchList> result = client.retrieve(request, WatchList.class).singleOrError();
        LOG.debug(result.blockingGet().getSymbols().toString());
        Assertions.assertEquals(3, result.blockingGet().getSymbols().size());
        Assertions.assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void canUpdateWatchListForAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        var request = HttpRequest.PUT("/account/watchlist-reactive", watchList)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());

        final HttpResponse<Object> response = client.toBlocking().exchange(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }


    @Test
    void canDeleteWatchlistForAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();
        final var symbols = Stream.of("APPL", "AMZN", "GOOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);
        var request = HttpRequest.DELETE("/account/watchlist-reactive/" + TEST_ACCOUNT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());

        final HttpResponse<Object> response = client.toBlocking().exchange(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }
}
