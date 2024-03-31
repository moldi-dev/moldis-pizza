package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.getAllOrdersByUserId(userId);
    }

    public List<Order> getAllOrdersByUsername(String username) {
        return orderRepository.getAllOrdersByUsername(username);
    }

    public Order addOrderByUserIdBasket(Long userId) {
        Optional<Basket> userBasket = basketRepository.getBasketByUserId(userId);
        Optional<Double> totalOrderPrice = basketRepository.getBasketTotalPriceByUserId(userId);
        Optional<User> user = userRepository.findById(userId);

        if (userBasket.isPresent() && totalOrderPrice.isPresent() && user.isPresent()) {
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

        return null;
    }

    public Order addOrderByUsernameBasket(String username) {
        Optional<Basket> userBasket = basketRepository.getBasketByUsername(username);
        Optional<Double> totalOrderPrice = basketRepository.getBasketTotalPriceByUsername(username);
        Optional<User> user = userRepository.findUserByUsername(username);

        if (userBasket.isPresent() && totalOrderPrice.isPresent() && user.isPresent()) {
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

        return null;
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}
