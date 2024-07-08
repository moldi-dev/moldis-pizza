package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDTO(UUID orderId,
                       UserDTO user,
                       List<PizzaDTO> pizzas,
                       Double totalPrice,
                       LocalDateTime createdDate,
                       OrderStatus status) {
}
