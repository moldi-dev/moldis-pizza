package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface OrderService {
    OrderDTO save(Order order);

    OrderDTO findById(Long orderId);
    Page<OrderDTO> findAll(int page, int size);
    Page<OrderDTO> findAllByUserId(Long userId, int page, int size);

    OrderDTO updateById(Long orderId, Order updatedOrder);

    OrderDTO placeOrderByUserBasket(Long userId);

    void deleteById(Long orderId);
}
