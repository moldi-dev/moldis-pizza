package org.moldidev.moldispizza.mapper;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PizzaDTOMapper implements Function<Pizza, PizzaDTO> {
    private final ImageDTOMapper imageDTOMapper;

    public PizzaDTOMapper(ImageDTOMapper imageDTOMapper) {
        this.imageDTOMapper = imageDTOMapper;
    }

    @Override
    public PizzaDTO apply(Pizza pizza) {
        return new PizzaDTO(
                pizza.getPizzaId(),
                pizza.getName(),
                pizza.getImages()
                        .stream()
                        .map(imageDTOMapper::apply)
                        .collect(Collectors.toList()),
                pizza.getIngredients(),
                pizza.getPrice()
        );
    }
}
