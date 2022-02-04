package com.example.broker;

import com.example.broker.model.Symbol;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.annotation.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller("/markets")
public class MarketsController {

    private final InMemoryStore store;

    public MarketsController(final InMemoryStore store) {
        this.store = store;
    }

    public List<Symbol> all() {
        return store.getAllSymbols();
    }
}
