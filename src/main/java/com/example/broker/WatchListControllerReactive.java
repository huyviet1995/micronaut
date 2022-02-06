package com.example.broker;

import com.example.broker.model.WatchList;
import com.example.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.reactivex.Single;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Controller("/account/watchlist-reactive")
public class WatchListControllerReactive {

    private final InMemoryAccountStore store;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactive.class);
    private final Scheduler scheduler;

    public WatchListControllerReactive(final InMemoryAccountStore store, @Named(TaskExecutors.IO) ExecutorService executorService) {
        this.store = store;
        this.scheduler = Schedulers.from(executorService);
    }

    @Get(value="/single", produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public Single<WatchList> getAsSingle() {
        LOG.debug("getAsSingle {}", Thread.currentThread().getName());
        return Single.fromCallable(() -> {
            LOG.debug("getAsSingle {}", Thread.currentThread().getName());
            return store.getWatchList(ACCOUNT_ID);
        }).subscribeOn(scheduler);
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
