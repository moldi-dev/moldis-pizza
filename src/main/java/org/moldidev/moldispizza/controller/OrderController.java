package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.service.BasketService;
import org.moldidev.moldispizza.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();

        if (orderList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);

        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getAllOrdersByUserId/{userId}")
    public ResponseEntity<List<Order>> getAllOrdersByUserId(@PathVariable Long userId) {
        List<Order> orderList = orderService.getAllOrdersByUserId(userId);

        if (orderList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/getAllOrdersByUsername/{username}")
    public ResponseEntity<List<Order>> getAllOrdersByUsername(@PathVariable String username) {
        List<Order> orderList = orderService.getAllOrdersByUsername(username);

        if (orderList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PostMapping("/addOrderByUserIdBasket/{userId}")
    public ResponseEntity<Order> addOrderByUserIdBasket(@PathVariable Long userId) {
        Order addedOrder = orderService.addOrderByUserIdBasket(userId);

        if (addedOrder != null) {
            return new ResponseEntity<>(addedOrder, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addOrderByUsernameBasket/{username}")
    public ResponseEntity<Order> addOrderByUsernameBasket(@PathVariable String username) {
        Order addedOrder = orderService.addOrderByUsernameBasket(username);

        if (addedOrder != null) {
            return new ResponseEntity<>(addedOrder, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteOrderById/{id}")
    public ResponseEntity<Order> deleteOrderById(@PathVariable Long id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrderById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
