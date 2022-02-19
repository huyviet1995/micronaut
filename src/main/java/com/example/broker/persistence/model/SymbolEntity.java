package com.example.broker.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="symbol")
@Table(name="symbols", schema = "mn")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymbolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;

    public SymbolEntity(String value) {
        this.value = value;
    }
}
