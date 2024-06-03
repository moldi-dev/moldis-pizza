package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.enumeration.OrderStatus;

import java.util.Date;
import java.util.List;

public record OrderDTO(Long orderId,
                       UserDTO user,
                       List<PizzaDTO> pizzas,
                       Double totalPrice,
                       Date createdAt,
                       OrderStatus status) {
}
