
package com.example;

import com.example.broker.MarketsController;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/hello")
public class HelloWorldController {

    private final HelloWorldService service;
    private final GreetingConfig config;
    private final Logger LOG = LoggerFactory.getLogger(MarketsController.class);

    public HelloWorldController(final HelloWorldService service, final GreetingConfig config) {
        this.service = service;
        this.config = config;
    }

    @Get("/")
    public String index() {
        return service.sayHi();
    }

    @Get("/de")
    public String greetInGerman() {
        return config.getDe();
    }

    @Get("/en")
    public String greetInEnglish() {
        return config.getEn();
    }

    @Get("/json")
    public Greeting json(){
        return new Greeting();
    }

}
