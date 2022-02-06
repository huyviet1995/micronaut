package com.example.broker;

import com.example.broker.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public class WatchListController {

    private final InMemoryAccountStore store;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();

    public WatchListController(final InMemoryAccountStore store) {
        this.store = store;
    }

    @Get("/")
    public HttpResponse<WatchList> get() {
        return HttpResponse.ok().body(store.getWatchList(ACCOUNT_ID));
    }

    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(value="/{accountId}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public void delete(@PathVariable UUID accountId) {
       store.deleteWatchList(accountId);
    }
}
