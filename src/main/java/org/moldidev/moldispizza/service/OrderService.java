package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.dto.OrderDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final OrderDTOMapper orderDTOMapper;

    public OrderService(OrderRepository orderRepository, BasketRepository basketRepository, UserRepository userRepository, OrderDTOMapper orderDTOMapper) {
        this.orderRepository = orderRepository;
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.orderDTOMapper = orderDTOMapper;
    }

    public List<OrderDTO> getAllOrders() throws ResourceNotFoundException {
        List<Order> orderList = orderRepository.findAll();

        if (!orderList.isEmpty()) {
            return orderList.stream().map(orderDTOMapper).collect(Collectors.toList());
        }

        throw new ResourceNotFoundException("there are no orders");
    }

    public OrderDTO getOrderById(Long id) throws ResourceNotFoundException {
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isPresent()) {
            return orderDTOMapper.apply(foundOrder.get());
        }

        throw new ResourceNotFoundException("order not found by id: " + id);
    }

    public List<OrderDTO> getAllOrdersByUserId(Long userId) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(userId);
        List<Order> orderList = orderRepository.getAllOrdersByUserId(userId);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + userId);
        }

        if (!orderList.isEmpty()) {
            return orderList.stream().map(orderDTOMapper).collect(Collectors.toList());
        }

        throw new ResourceNotFoundException("user with id " + userId + " has no orders");
    }

    public List<OrderDTO> getAllOrdersByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        List<Order> orderList = orderRepository.getAllOrdersByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        if (!orderList.isEmpty()) {
            return orderList.stream().map(orderDTOMapper).collect(Collectors.toList());
        }

        throw new ResourceNotFoundException(username + " has no orders");
    }

    public OrderDTO addOrderByUserIdBasket(Long userId) throws ResourceNotFoundException {
        Optional<Basket> userBasket = basketRepository.getBasketByUserId(userId);
        Optional<Double> totalOrderPrice = basketRepository.getBasketTotalPriceByUserId(userId);
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + userId);
        }

        else if (userBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + userId);
        }

        else if (userBasket.get().getPizzaList().isEmpty()) {
            throw new ResourceNotFoundException(user.get().getUsername() + " has an empty basket");
        }

        else if (totalOrderPrice.isEmpty()) {
            throw new ResourceNotFoundException("total basket price not found by user id: " + userId);
        }

        Order orderToAdd = new Order();

        orderToAdd.setUser(user.get());
        List<Pizza> pizzasInOrder = new ArrayList<>(userBasket.get().getPizzaList());
        orderToAdd.setPizzaList(pizzasInOrder);
        orderToAdd.setDate(java.sql.Date.valueOf(LocalDate.now()));
        orderToAdd.setPrice(totalOrderPrice.get());

        userBasket.get().getPizzaList().clear();

        basketRepository.save(userBasket.get());

        Order savedOrder = orderRepository.save(orderToAdd);

        return orderDTOMapper.apply(savedOrder);
    }

    public OrderDTO addOrderByUsernameBasket(String username) {
        Optional<Basket> userBasket = basketRepository.getBasketByUsername(username);
        Optional<Double> totalOrderPrice = basketRepository.getBasketTotalPriceByUsername(username);
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (userBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        else if (userBasket.get().getPizzaList().isEmpty()) {
            throw new ResourceNotFoundException(username + " has an empty basket");
        }

        else if (totalOrderPrice.isEmpty()) {
            throw new ResourceNotFoundException("total basket price not found by username: " + username);
        }

        Order orderToAdd = new Order();

        orderToAdd.setUser(user.get());
        List<Pizza> pizzasInOrder = new ArrayList<>(userBasket.get().getPizzaList());
        orderToAdd.setPizzaList(pizzasInOrder);
        orderToAdd.setDate(java.sql.Date.valueOf(LocalDate.now()));
        orderToAdd.setPrice(totalOrderPrice.get());

        userBasket.get().getPizzaList().clear();

        basketRepository.save(userBasket.get());

        Order savedOrder = orderRepository.save(orderToAdd);

        return orderDTOMapper.apply(savedOrder);
    }

    @Transactional
    public void deleteOrderById(Long id) throws ResourceNotFoundException {
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isPresent()) {
            orderRepository.deleteById(id);
        }

        else {
            throw new ResourceNotFoundException("order not found by id: " + id);
        }
    }
}
