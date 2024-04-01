package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/getAllOrdersByUserId/{userId}")
    public ResponseEntity<List<Order>> getAllOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userId));
    }

    @GetMapping("/getAllOrdersByUsername/{username}")
    public ResponseEntity<List<Order>> getAllOrdersByUsername(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getAllOrdersByUsername(username));
    }

    @PostMapping("/addOrderByUserIdBasket/{userId}")
    public ResponseEntity<Order> addOrderByUserIdBasket(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.addOrderByUserIdBasket(userId));
    }

    @PostMapping("/addOrderByUsernameBasket/{username}")
    public ResponseEntity<Order> addOrderByUsernameBasket(@PathVariable String username) {
        return ResponseEntity.ok(orderService.addOrderByUsernameBasket(username));
    }

    @DeleteMapping("/deleteOrderById/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
