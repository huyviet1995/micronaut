package com.example.broker;

import com.example.broker.model.Symbol;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;
import java.util.stream.Stream;

@Controller("/markets")
public class MarketsController {

    private final InMemoryStore store;

    public MarketsController(final InMemoryStore store) {
        this.store = store;
    }

    @Get("/")
    public HttpResponse<List<Symbol>> all() {
        return HttpResponse.ok().body( store.getAllSymbols() );
    }
}
