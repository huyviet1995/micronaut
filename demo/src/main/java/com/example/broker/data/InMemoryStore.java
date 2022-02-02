package com.example.broker.data;

import com.example.broker.Symbol;
import com.github.javafaker.Faker;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
public class InMemoryStore {
    private final Map<String, Symbol> symbols = new HashMap<String, Symbol>();
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
    private final Faker faker = new Faker();

    @PostConstruct
    public void initialize() {
        IntStream.range(0, 10).forEach(i -> addNewSymbol());
    }

    private void addNewSymbol() {
        var symbol = new Symbol(faker.stock().nsdqSymbol());
        symbols.put(symbol.getValue(), symbol);
        LOG.debug("Added symbol");
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
}
