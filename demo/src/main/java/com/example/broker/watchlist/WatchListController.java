package com.example.broker.watchlist;

import com.example.broker.data.InMemoryAccountStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.Media;
import java.util.UUID;

@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {
    static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final Logger LOG = LoggerFactory.getLogger(WatchListController.class);

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete("{accountId}")
    public String delete(@PathVariable String accountId) {
        store.deleteWatchList(UUID.fromString(accountId));
        LOG.debug(store.getWatchList(UUID.fromString(accountId)).toString());
        return accountId;
    }
}
