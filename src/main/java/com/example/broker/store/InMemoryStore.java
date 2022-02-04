package com.example.broker.store;

import com.example.broker.model.Symbol;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private final List<Symbol> symbols;
    private final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);

    public InMemoryStore() {
        symbols = Stream.of("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "FPT")
                .map(symbol -> new Symbol(symbol))
                .collect(Collectors.toList());
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }
}
