package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public interface OrderService {
    OrderDTO save(Order order);

    OrderDTO findById(UUID orderId, Authentication connectedUser);
    OrderDTO findById(UUID orderId);

    Page<OrderDTO> findAll(int page, int size);
    Page<OrderDTO> findAllByUserId(Long userId, int page, int size, Authentication connectedUser);

    Boolean hasUserBoughtThePizza(Long userId, Long pizzaId, Authentication connectedUser);

    OrderDTO updateById(UUID orderId, Order updatedOrder);
    OrderDTO setOrderAsPaid(UUID orderId, Authentication connectedUser);

    OrderDTO placeOrderByUserBasket(Long userId, Authentication connectedUser);

    void deleteById(UUID orderId);
}
