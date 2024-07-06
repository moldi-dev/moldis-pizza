package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface OrderService {
    OrderDTO save(Order order);

    OrderDTO findById(Long orderId);
    Page<OrderDTO> findAll(int page, int size);
    Page<OrderDTO> findAllByUserId(Long userId, int page, int size, Authentication connectedUser);

    Boolean hasUserBoughtThePizza(Long userId, Long pizzaId, Authentication connectedUser);

    OrderDTO updateById(Long orderId, Order updatedOrder);

    OrderDTO placeOrderByUserBasket(Long userId, Authentication connectedUser);

    void deleteById(Long orderId);
}
