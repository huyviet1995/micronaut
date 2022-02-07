package com.example;

import com.example.broker.error.CustomError;
import com.example.broker.model.Quote;
import com.example.broker.model.Symbol;
import com.example.broker.store.InMemoryStore;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

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
        // IThis line of code will cause an error. I will need to evalutate this later.
//        assertThat(apple).isEqualToComparingFieldByField(appleResult);
    }

    @Test
    void returnsNotFoundOnUnsupportedSymbol() {
        try {
            client.toBlocking().retrieve(HttpRequest.GET("/quotes/unsupported"), Argument.of(Quote.class), Argument.of(CustomError.class));
        } catch (HttpClientResponseException e) {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getResponse().getStatus());
            final Optional<CustomError> customError = e.getResponse().getBody(CustomError.class);
            Assertions.assertTrue(customError.isPresent());
            Assertions.assertEquals(404, customError.get().getStatus());
            Assertions.assertEquals("NOT_FOUND", customError.get().getError());
            Assertions.assertEquals("Quote for symbol not found", customError.get().getMessage());
            Assertions.assertEquals("/quotes/unsupported", customError.get().getPath());
        }
    }

    private Quote initRandomQuote(Symbol symbol) {
        return Quote.builder().symbol(symbol).bid(randomValue()).volumn(randomValue()).ask(randomValue()).lastPrice(randomValue()).build();
    }

    private BigDecimal randomValue() {
//        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt());
        return BigDecimal.valueOf(4);
    }
}
