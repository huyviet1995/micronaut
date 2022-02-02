package com.example.broker;

import com.example.broker.data.InMemoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Controller("/symbols")
public class SymbolsController {

    private final InMemoryStore inMemoryStore;

    public SymbolsController(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    @Get
    public ArrayList<Symbol> getAll() {
        return new ArrayList<Symbol>(inMemoryStore.getSymbols().values());
    }

    @Get("{value}")
    public Symbol getSymbolByValue(@PathVariable String value) {
        return inMemoryStore.getSymbols().get(value);
    }
}
