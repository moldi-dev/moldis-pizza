package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.entity.Order;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderDTOMapper implements Function<Order, OrderDTO> {

    UserDTOMapper userDTOMapper = new UserDTOMapper();

    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(order.getId(),
                userDTOMapper.apply(order.getUser()),
                order.getPizzaList(),
                order.getPrice(),
                order.getDate());
    }
}
