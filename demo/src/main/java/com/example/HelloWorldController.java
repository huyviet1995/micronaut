package com.example;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/hello")
public class HelloWorldController {

    @Inject
    private final MyService service;

    private final String helloFromConfig;

    private final HelloWorldTranslationConfig translationConfig;

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldController.class);

    public HelloWorldController(MyService service,
                                @Property(name = "hello.world.message")
                                        String helloFromConfig, HelloWorldTranslationConfig translationConfig) {
        this.service = service;
        this.helloFromConfig = helloFromConfig;
        this.translationConfig = translationConfig;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld() {
        LOG.debug("Called the Hello World API");
        return service.helloFromService();
    }

    @Get(uri = "/config", produces = MediaType.TEXT_PLAIN)
    public String helloConfig() {
        LOG.debug("Return Hello From Config Message: {}", helloFromConfig);
        return helloFromConfig;
    }

    @Get(uri = "/translation", produces = MediaType.APPLICATION_JSON)
    public HelloWorldTranslationConfig helloTranslation() {
        LOG.debug(translationConfig.toString());
        return translationConfig;
    }
}