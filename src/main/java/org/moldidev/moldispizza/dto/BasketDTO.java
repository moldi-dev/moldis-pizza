package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.entity.Pizza;

import java.util.List;

public record BasketDTO(
        Long id,
        UserDTO userDTO,
        List<Pizza> pizzaList
) {
}
