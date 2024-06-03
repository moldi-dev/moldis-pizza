package org.moldidev.moldispizza.dto;

import java.util.List;

public record BasketDTO(Long basketId,
                        UserDTO user,
                        Double totalPrice,
                        List<PizzaDTO> pizzas) {
}
