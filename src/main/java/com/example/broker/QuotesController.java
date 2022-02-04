package com.example.broker;

import com.example.broker.model.Quote;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.Optional;

@Controller("/quotes")
public class QuotesController {
    private final InMemoryStore store;

    public QuotesController(final InMemoryStore store) {
        this.store = store;
    }

    @Get("/{symbol}")
    public HttpResponse<Quote> getQuote(@PathVariable String symbol) {
        final Optional<Quote> quoteMaybe = store.fetchQuote(symbol);
        return HttpResponse.ok(quoteMaybe.get());
    }
}
