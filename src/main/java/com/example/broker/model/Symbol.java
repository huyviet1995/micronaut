package com.example.broker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Symbol {
    private String value;
    public Symbol(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String newValue) {
        this.value = newValue;
    }
}
