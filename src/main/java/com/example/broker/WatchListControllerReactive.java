package com.example.broker;

import com.example.broker.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller("/account/watchlist-reactive")
public class WatchListControllerReactive {

    private final InMemoryAccountStore store;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactive.class);

    public WatchListControllerReactive(final InMemoryAccountStore store) {
        this.store = store;
    }

    @Get("/")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<WatchList> get() {
        LOG.debug("Get watchlist {}", Thread.currentThread().getName());
        return HttpResponse.ok().body(store.getWatchList(ACCOUNT_ID));
    }

    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(value="/{accountId}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public void delete(@PathVariable UUID accountId) {
       store.deleteWatchList(accountId);
    }
}
