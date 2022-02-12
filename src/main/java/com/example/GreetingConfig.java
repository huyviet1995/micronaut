package com.example;

import javax.validation.constraints.NotBlank;

import io.micronaut.context.annotation.ConfigurationInject;
import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@ConfigurationProperties("hello.config.greeting")
@Data
public class GreetingConfig {

    private final String de;
    private final String en;

    @ConfigurationInject
    public GreetingConfig(@NotBlank final String de, @NotBlank final String en) {
        this.de = de;
        this.en = en;
    }

}