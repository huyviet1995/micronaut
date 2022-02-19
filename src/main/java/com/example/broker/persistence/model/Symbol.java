package com.example.broker.persistence.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="Symbol", description = "Abbreviation to uniquely identify public trade shares of a stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Symbol {
    @Schema(description = "Symbol value", minLength = 1, maxLength = 5)
    private String value;
}
