package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.entity.Pizza;

import java.util.Date;
import java.util.List;

public record OrderDTO(
        Long id,
        UserDTO userDTO,
        List<Pizza> pizzaList,
        double price,
        Date date
) {
}
