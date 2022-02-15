package com.example.broker.persistence.jpa;

import com.example.broker.model.SymbolEntity;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * Used to insert data into db on startup.
 */
@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
    private final SymbolsRepository symbols;

    public TestDataProvider(final SymbolsRepository symbols) {
        this.symbols = symbols;
    }

    @EventListener
    public void init(StartupEvent event) {
        LOG.debug("Adding test data for empty database was found!");
        if (symbols.findAll().isEmpty()) {
            Stream.of("APPL", "AMZN", "FB", "TSLA").map(SymbolEntity::new).forEach(symbols::save);
        }
    }
}
