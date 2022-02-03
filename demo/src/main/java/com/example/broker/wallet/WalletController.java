package com.example.broker.wallet;

import com.example.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.print.attribute.standard.Media;
import java.util.Collection;
import java.util.List;

@Controller("/account/wallets")
public record WalletController(InMemoryAccountStore store) {
    public static final List<String> SUPPORTED_FIAT_CURRENCY = List.of("EUR", "USD", "CRF", "GBP");

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet> get() {
        return store.getWallets(InMemoryAccountStore.ACCOUNT_ID);
    }

    @Post(value="/deposit", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Void> depositFiatMoney(@Body DepositFiatMoney deposit) {
        return HttpResponse.ok();
    }

    @Post(value="/withdraw", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Void> withdrawFiatMoney(@Body WithdrawFiatMoney withdraw) {
        return HttpResponse.ok();
    }

}
