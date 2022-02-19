package com.example.broker;

import com.example.broker.error.CustomError;
import com.example.broker.persistence.jpa.QuotesRepository;
import com.example.broker.persistence.model.Quote;
import com.example.broker.persistence.model.QuoteDTO;
import com.example.broker.persistence.model.QuoteEntity;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {
    private final InMemoryStore store;

    private final QuotesRepository quotes;

    public QuotesController(final InMemoryStore store, final QuotesRepository quotes) {
        this.store = store;
        this.quotes = quotes;
    }

    @Operation(summary = "Returns a quote for a given symbol")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid Symbol specified")
    @Tag(name="Quotes")
    @Get("/{symbol}")
    public HttpResponse getQuote(@PathVariable String symbol) {
        final Optional<Quote> quoteMaybe = store.fetchQuote(symbol);
        if (quoteMaybe.isEmpty()) {
            final CustomError notFound = CustomError
                    .builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("Quote for symbol not found")
                    .path("/quotes/" + symbol)
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(quoteMaybe.get());
    }

    @Operation(summary = "Returns a quote for a given symbol. Fetch from the database via JPA")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid Symbol specified")
    @Tag(name="quotes")
    @Get("/{symbol}/jpa")
    public HttpResponse getQuoteViaJPA(@PathVariable String symbol) {
        final Optional<QuoteDTO> quoteMaybe = quotes.findBySymbolValue(symbol);
        if (quoteMaybe.isEmpty()) {
            final CustomError notFound = CustomError
                    .builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("Quotes for symbol not available in db.")
                    .path("/quotes/" + symbol + "/jpa")
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(quoteMaybe.get());
    }

    @Get("/jpa")
    public List<QuoteEntity> getAllQuotesViaJPA() {
        return quotes.findAll();
    }

    @Get("/jpa/ordered/desc")
    public List<QuoteDTO> orderedDesc() {
        return quotes.listOrderByVolumeDesc();
    }

    @Get("/jpa/ordered/asc")
    public List<QuoteDTO> listOrderByVolumeAsc() {
       return quotes.listOrderByVolumeAsc();
    }
}
