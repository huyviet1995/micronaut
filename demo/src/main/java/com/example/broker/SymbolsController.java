package com.example.broker;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;

@Controller("/symbols")
public class SymbolsController {
    @Get
    public ArrayList<Symbol> getAll() {
        return new ArrayList<Symbol>();
    }
}
