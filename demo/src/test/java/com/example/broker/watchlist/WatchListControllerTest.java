package com.example.broker.watchlist;

import com.example.broker.Symbol;
import com.example.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Stream;

@MicronautTest
public class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = InMemoryAccountStore.ACCOUNT_ID;

    @Inject
    @Client("/account/watchlist")
    HttpClient client;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setup() {
//        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    void returnsEmptyWatchListForTestAccount() {
        final WatchList result = client.toBlocking().retrieve(HttpRequest.GET("/"), WatchList.class);
        Assertions.assertNull(result.symbols());
        Assertions.assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }

    @Test
    void returnsWatchListForTestAccount() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT").map(Symbol::new).toList()
        ));

        var response = client.toBlocking().exchange("/", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        // Asserts equal to a certain response here.
    }

    @Test
    void canUpdateWatchListForTestAccount() {
        var symbols = Stream.of("AAPL", "GOOGL", "MSFT").map(Symbol::new).toList();
        final var request = HttpRequest.PUT("/", new WatchList(symbols)).accept(MediaType.APPLICATION_JSON);
        final HttpResponse<Object> added = client.toBlocking().exchange(request);
        Assertions.assertEquals(HttpStatus.OK, added.getStatus());
        Assertions.assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());
    }

    @Test
    void canDeleteWatchList() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(Stream.of("AAPL").map(Symbol::new).toList()));
        final var request = HttpRequest.DELETE("/" + TEST_ACCOUNT_ID.toString());
        LOG.debug(request.getUri().toString());
        final HttpResponse response = client.toBlocking().exchange(request, String.class);
        // Check if the response is actually the accountId.
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(TEST_ACCOUNT_ID.toString(), response.getBody().get());
        // Check if the
        Assertions.assertEquals(0, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().size());
    }
}
