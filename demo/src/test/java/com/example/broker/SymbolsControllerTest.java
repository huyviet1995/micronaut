package com.example.broker;

import com.example.broker.data.InMemoryStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.JsonObjectSerializer;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.json.tree.JsonObject;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@MicronautTest
public class SymbolsControllerTest {

    @Inject
    @Client("/symbols")
    HttpClient client;

    @BeforeEach
    void setup() {
        inMemoryStore.initializeWith(10);
    }
    private static final Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    InMemoryStore inMemoryStore;

    @Test
    void symbolsEndpointReturnsListOfSymbol() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbolsEndpointReturnsTheCorrectSymbol() throws JsonProcessingException {
        var testSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(testSymbol.value(), testSymbol);
        var response = client.toBlocking().exchange("/" + testSymbol.value(), Symbol.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(testSymbol, response.getBody().get());
    }
}
