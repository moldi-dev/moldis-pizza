package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.enumeration.OrderStatus;
import org.moldidev.moldispizza.exception.ObjectNotValidException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.OrderDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.service.OrderService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDTOMapper orderDTOMapper;
    private final ObjectValidator<Order> objectValidator;
    private final BasketRepository basketRepository;

    @Override
    public OrderDTO save(Order order) {
        objectValidator.validate(order);
        return orderDTOMapper.apply(orderRepository.save(order));
    }

    @Override
    public OrderDTO findById(Long orderId) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found by id " + orderId));

        return orderDTOMapper.apply(foundOrder);
    }

    @Override
    public Page<OrderDTO> findAll(int page, int size) {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, size));

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found");
        }

        return orders.map(orderDTOMapper);
    }

    @Override
    public Page<OrderDTO> findAllByUserId(Long userId, int page, int size) {
        Page<Order> orders = orderRepository.findAllByUserUserId(userId, PageRequest.of(page, size));

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found by user id " + userId);
        }

        return orders.map(orderDTOMapper);
    }

    @Override
    public OrderDTO updateById(Long orderId, Order updatedOrder) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found by id " + orderId));

        objectValidator.validate(updatedOrder);

        foundOrder.setPizzas(updatedOrder.getPizzas());
        foundOrder.setStatus(updatedOrder.getStatus());
        foundOrder.setTotalPrice(updatedOrder.getTotalPrice());
        foundOrder.setUser(updatedOrder.getUser());

        return orderDTOMapper.apply(orderRepository.save(foundOrder));
    }

    @Override
    public OrderDTO placeOrderByUserBasket(Long userId) {
        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by user id " + userId));

        if (foundBasket.getPizzas().isEmpty()) {
            HashSet<String> violations = new HashSet<>();
            violations.add("The pizza list is required");
            throw new ObjectNotValidException(violations);
        }

        Order placedOrder = new Order();

        placedOrder.setPizzas(foundBasket.getPizzas());
        placedOrder.setUser(foundBasket.getUser());
        placedOrder.setStatus(OrderStatus.PENDING);
        placedOrder.setTotalPrice(foundBasket.getTotalPrice());

        foundBasket.setTotalPrice(0.0);
        foundBasket.setPizzas(new ArrayList<>());

        basketRepository.save(foundBasket);

        return orderDTOMapper.apply(orderRepository.save(placedOrder));
    }

    @Override
    public void deleteById(Long orderId) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found by id " + orderId));

        orderRepository.delete(foundOrder);
    }
}
