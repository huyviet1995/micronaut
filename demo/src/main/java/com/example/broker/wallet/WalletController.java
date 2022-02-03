package com.example.broker.wallet;

import com.example.broker.api.RestApiResponse;
import com.example.broker.data.InMemoryAccountStore;
import com.example.broker.wallet.error.CustomError;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.Media;
import java.util.Collection;
import java.util.List;

@Controller("/account/wallets")
public record WalletController(InMemoryAccountStore store) {
    public static final List<String> SUPPORTED_FIAT_CURRENCY = List.of("EUR", "USD", "CRF", "GBP");
    private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet> get() {
        return store.getWallets(InMemoryAccountStore.ACCOUNT_ID);
    }

    @Post(value="/deposit", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<RestApiResponse> depositFiatMoney(@Body DepositFiatMoney deposit) {
        if (!SUPPORTED_FIAT_CURRENCY.contains(deposit.symbol().value())) {
            return HttpResponse.badRequest().body(new CustomError(
                    HttpStatus.BAD_REQUEST.getCode(),
                    "UNSUPPORTED FIAT CURRENCY",
                    String.format("Only %s are supported as currency", SUPPORTED_FIAT_CURRENCY)
            ));
        }
        var wallet = store.depositToWallet(deposit)
        return HttpResponse.ok();
    }

    @Post(value="/withdraw", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Void> withdrawFiatMoney(@Body WithdrawFiatMoney withdraw) {
        return HttpResponse.ok();
    }

}
