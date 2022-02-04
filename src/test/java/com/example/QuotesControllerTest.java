package com.example;

import com.example.broker.model.Quote;
import com.example.broker.model.Symbol;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@MicronautTest
public class QuotesControllerTest {

    @Inject
    EmbeddedApplication application;

    @Inject
    @Client("/") HttpClient client;

    @Inject InMemoryStore store;

    @Test
    void returnsQuotePerSymbol() {
        var apple = new Quote(new Symbol("APPL"), randomValue(), randomValue(), randomValue(), randomValue());
        store.update(apple);
        final var appleResult = client.toBlocking().retrieve(HttpRequest.GET("/quotes/APPL"), JsonNode.class);
//        assertThat(apple).isEqualToComparingFieldByField(appleResult);
    }

    private BigDecimal randomValue() {
//        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt());
        return BigDecimal.valueOf(4);
    }
}
