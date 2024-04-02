package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PizzaDTOMapper implements Function<Pizza, PizzaDTO> {
    @Override
    public PizzaDTO apply(Pizza pizza) {
        return new PizzaDTO(pizza.getId(),
                pizza.getName(),
                pizza.getIngredients(),
                pizza.getPrice());
    }
}
