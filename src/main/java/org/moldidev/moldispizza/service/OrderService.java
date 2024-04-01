package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, BasketRepository basketRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getAllOrders() throws ResourceNotFoundException {
        List<Order> orderList = orderRepository.findAll();

        if (!orderList.isEmpty()) {
            return orderList;
        }

        throw new ResourceNotFoundException("there are no orders");
    }

    public Order getOrderById(Long id) throws ResourceNotFoundException {
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isPresent()) {
            return foundOrder.get();
        }

        throw new ResourceNotFoundException("order not found by id: " + id);
    }

    public List<Order> getAllOrdersByUserId(Long userId) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(userId);
        List<Order> orderList = orderRepository.getAllOrdersByUserId(userId);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + userId);
        }

        if (!orderList.isEmpty()) {
            return orderList;
        }

        throw new ResourceNotFoundException("user with id " + userId + " has no orders");
    }

    public List<Order> getAllOrdersByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        List<Order> orderList = orderRepository.getAllOrdersByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        if (!orderList.isEmpty()) {
            return orderList;
        }

        throw new ResourceNotFoundException(username + " has no orders");
    }

    public Order addOrderByUserIdBasket(Long userId) throws ResourceNotFoundException {
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

        return orderRepository.save(orderToAdd);
    }

    public Order addOrderByUsernameBasket(String username) {
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

        return orderRepository.save(orderToAdd);
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
