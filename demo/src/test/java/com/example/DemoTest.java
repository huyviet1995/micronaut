package com.example;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

@MicronautTest
class DemoTest {

    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void helloWorldEndpointRespondsWithProperContent() {
        var response = client.toBlocking().retrieve("/hello");
        Assertions.assertEquals("Hello From Service", response);;
    }

    @Test
    void helloWorldEndpointRespondsWithProperStatusCodeAndContent() {
        var response  = client.toBlocking().exchange("/hello", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals("Hello From Service", response.getBody().get());
    }

    @Test
    void helloFromConfigEndpointReturnsMessageFromConfigFile() {
        var response = client.toBlocking().exchange("hello/config", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals("Hello from application.xml", response.getBody().get());
    }

//    @Test
//    void helloFromTranslationConfigEndpointReturnsMessageFromConfigFile() {
//        var response = client.toBlocking().exchange("/hello/translation", JsonNode.class);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
//        Assertions.assertEquals("Hello from application.xml", response.getBody().get().toString());
//    }
}
