package com.example.broker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
public class Quote {
    private Symbol symbol;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal lastPrice;
    private BigDecimal volumn;

    public Quote() {
        super();
    }

    @JsonCreator
    public Quote(
        final Symbol symbol,
         final BigDecimal bid,
         final BigDecimal ask,
         final BigDecimal lastPrice,
         final BigDecimal volumn
    ) {
        this.symbol = symbol;
        this.bid = bid;
        this.ask = ask;
        this.lastPrice = lastPrice;
        this.volumn = volumn;
    }

    public Symbol getSymbol() {
        return symbol;
    }
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getBid() {
        return bid;
    }
    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }
    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }
    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getVolumn() {
        return volumn;
    }
    public void setVolumn(BigDecimal volumn) {
        this.volumn = volumn;
    }
}
