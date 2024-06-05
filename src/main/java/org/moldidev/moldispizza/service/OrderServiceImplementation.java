package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.OrderStatus;
import org.moldidev.moldispizza.exception.InvalidInputException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.OrderDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final OrderDTOMapper orderDTOMapper;

    public OrderServiceImplementation(OrderRepository orderRepository, OrderDTOMapper orderDTOMapper, UserRepository userRepository, BasketRepository basketRepository) {
        this.orderRepository = orderRepository;
        this.orderDTOMapper = orderDTOMapper;
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
    }

    @Override
    public OrderDTO save(Order order) {
        checkIfOrderIsValid(order);

        return orderDTOMapper.apply(orderRepository.save(order));
    }

    @Override
    public OrderDTO placeOrderByUserId(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by id " + userId));

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by id " + userId));

        Order order = new Order();

        order.setPizzas(foundBasket.getPizzas());
        order.setUser(foundUser);
        order.setTotalPrice(foundBasket.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);

        foundBasket.setPizzas(new ArrayList<>());
        foundBasket.setTotalPrice(0.0);
        basketRepository.save(foundBasket);

        return orderDTOMapper.apply(orderRepository.save(order));
    }

    @Override
    public OrderDTO findById(Long orderId) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found by id " + orderId));

        return orderDTOMapper.apply(foundOrder);
    }

    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found");
        }

        return orders
                .stream()
                .map(orderDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findAllByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserUserId(userId);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found by user id " + userId);
        }

        return orders
                .stream()
                .map(orderDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateById(Long orderId, Order updatedOrder) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found by id " + orderId));

        checkIfOrderIsValid(updatedOrder);

        foundOrder.setPizzas(updatedOrder.getPizzas());
        foundOrder.setStatus(updatedOrder.getStatus());
        foundOrder.setTotalPrice(updatedOrder.getTotalPrice());
        foundOrder.setUser(updatedOrder.getUser());
        foundOrder.setCreatedAt(updatedOrder.getCreatedAt());

        return orderDTOMapper.apply(orderRepository.save(foundOrder));
    }

    @Override
    public void deleteById(Long orderId) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found by id " + orderId));

        orderRepository.delete(foundOrder);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserUserId(userId);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found by user id " + userId);
        }

        orderRepository.deleteAll(orders);
    }

    private void checkIfOrderIsValid(Order order) {
        if (order.getUser() == null) {
            throw new InvalidInputException("The user can't be null");
        }

        if (order.getTotalPrice() == null) {
            throw new InvalidInputException("The total price can't be null");
        }

        else if (order.getTotalPrice() <= 0) {
            throw new InvalidInputException("The total price must be positive");
        }

        else if (order.getPizzas() == null) {
            throw new InvalidInputException("The pizza list can't be null");
        }

        else if (order.getPizzas().isEmpty()) {
            throw new InvalidInputException("The pizza list can't be empty");
        }

        else if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }
    }
}
