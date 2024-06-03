package org.moldidev.moldispizza.dto;

import java.util.List;

public record PizzaDTO(Long pizzaId,
                       String name,
                       List<ImageDTO> images,
                       String ingredients,
                       Double price) {
}
