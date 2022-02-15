package com.example.broker;

import com.example.broker.model.SymbolEntity;
import com.example.broker.persistence.jpa.SymbolsRepository;
import com.example.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/markets")
public class MarketsController {

    private final InMemoryStore store;
    private final SymbolsRepository symbols;

    public MarketsController(final InMemoryStore store, SymbolsRepository symbols) {
        this.store = store;
        this.symbols = symbols;
    }

    @Operation(summary = "return all available markets")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets")
    @Get("/")
    public HttpResponse<List<String>> all() {
        return HttpResponse.ok().body( store.getAllSymbols().stream().map(entry -> entry.getValue()).collect(Collectors.toList()) );
//        return HttpResponse.ok().body(store.getAllSymbols());
    }

    @Operation(summary = "return all available markets from database using JPA")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets")
    @Get("/jpa")
    public Single<List<SymbolEntity>> allSymbolsViaJPA() {
        return Single.just(symbols.findAll());
    }
}
