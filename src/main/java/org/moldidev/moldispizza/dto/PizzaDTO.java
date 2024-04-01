package org.moldidev.moldispizza.dto;

public record PizzaDTO(
    Long id,
    String name,
    String ingredients,
    double price
) {
}
