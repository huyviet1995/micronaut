package com.example.broker;

import com.example.broker.error.CustomError;
import com.example.broker.model.Quote;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.netty.handler.codec.http2.Http2SecurityUtil;

import java.util.Optional;

@Controller("/quotes")
public class QuotesController {
    private final InMemoryStore store;

    public QuotesController(final InMemoryStore store) {
        this.store = store;
    }

    @Get("/{symbol}")
    public HttpResponse getQuote(@PathVariable String symbol) {
        final Optional<Quote> quoteMaybe = store.fetchQuote(symbol);
        if (quoteMaybe.isEmpty()) {
            final CustomError notFound = CustomError
                    .builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("Quote symbol not available")
                    .path("/quote" + symbol)
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(quoteMaybe.get());
    }
}
