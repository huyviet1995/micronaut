package com.example;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import static org.assertj.core.api.Assertions.assertThat;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

@MicronautTest
public class MarketsControllerTest {

    @Inject
    EmbeddedApplication application;

    @Inject
    @Client("/") HttpClient client;

    @Test
    void returnsListOfMarkets() {
        final List result = client.toBlocking().retrieve("/markets", List.class);
        Assertions.assertEquals(7, result.size());
        // Update later to reflect the returning response as Symbol
        final List<String> markets = result;
        assertThat(markets)
                .containsExactlyInAnyOrder("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "FPT");
    }
}
