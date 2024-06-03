package org.moldidev.moldispizza.mapper;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderDTOMapper implements Function<Order, OrderDTO> {
    private final UserDTOMapper userDTOMapper;
    private final PizzaDTOMapper pizzaDTOMapper;

    public OrderDTOMapper(UserDTOMapper userDTOMapper, PizzaDTOMapper pizzaDTOMapper) {
        this.userDTOMapper = userDTOMapper;
        this.pizzaDTOMapper = pizzaDTOMapper;
    }

    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                userDTOMapper.apply(order.getUser()),
                order.getPizzas()
                        .stream()
                        .map(pizzaDTOMapper::apply)
                        .collect(Collectors.toList()),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }
}
