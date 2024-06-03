package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderService {
    OrderDTO save(Order order);

    OrderDTO placeOrderByUserId(Long userId);

    OrderDTO findById(Long orderId);
    List<OrderDTO> findAll();
    List<OrderDTO> findAllByUserId(Long userId);

    OrderDTO updateById(Long orderId, Order updatedOrder);

    void deleteById(Long orderId);
    void deleteAllByUserId(Long userId);
}
