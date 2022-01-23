package com.example;

import jakarta.inject.Singleton;

@Singleton
public class HelloWorldService implements MyService {

    @Override
    public String helloFromService() {
        return "Hello From Service";
    }
}
