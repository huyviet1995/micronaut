package com.example.broker.store;

import com.example.broker.model.Quote;
import com.example.broker.model.Symbol;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private final List<Symbol> symbols;
    private final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
    private final Map<String, Quote> cachedQuotes = new HashMap<>();
    private final ThreadLocalRandom current = ThreadLocalRandom.current();

    public InMemoryStore() {
        symbols = Stream.of("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "FPT")
                .map(symbol -> new Symbol(symbol))
                .collect(Collectors.toList());
        symbols.forEach(symbol -> {
            cachedQuotes.put(symbol.getValue(), randomQuote(symbol));
        });
    }

    private Quote randomQuote(Symbol symbol) {
        return new Quote(symbol, randomValue(), randomValue(), randomValue(), randomValue());
    }

    // Generate randomValue
    private BigDecimal randomValue() {
        return BigDecimal.valueOf(current.nextDouble());
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }

    public Optional<Quote> fetchQuote(final String symbol) {
        return Optional.ofNullable(cachedQuotes.get(symbol));
    }


    public void update(final Quote quote) {
        cachedQuotes.put(quote.getSymbol().getValue(), quote);
    }
}
