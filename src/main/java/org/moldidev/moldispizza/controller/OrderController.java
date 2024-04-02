package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/get-order-by-id/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/get-all-orders-by-user-id/{userId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userId));
    }

    @GetMapping("/get-all-orders-by-username/{username}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUsername(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getAllOrdersByUsername(username));
    }

    @PostMapping("/add-order-by-user-id-basket/{userId}")
    public ResponseEntity<OrderDTO> addOrderByUserIdBasket(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.addOrderByUserIdBasket(userId));
    }

    @PostMapping("/add-order-by-username-basket/{username}")
    public ResponseEntity<OrderDTO> addOrderByUsernameBasket(@PathVariable String username) {
        return ResponseEntity.ok(orderService.addOrderByUsernameBasket(username));
    }

    @DeleteMapping("/delete-order-by-id/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
