package com.example;

import com.example.broker.model.Symbol;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(6, result.size());
    }
}
