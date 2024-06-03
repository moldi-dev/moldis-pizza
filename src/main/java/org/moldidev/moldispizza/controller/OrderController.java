package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderDTO>> find() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/find/id={order_id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable("order_id") Long order_id) {
        return ResponseEntity.ok(orderService.findById(order_id));
    }

    @GetMapping("/find-all/user-id={user_id}")
    public ResponseEntity<List<OrderDTO>> findByUserId(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(orderService.findAllByUserId(userId));
    }

    @GetMapping("/place-order/user-id={user_id}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(orderService.placeOrderByUserId(userId));
    }

    @PostMapping("/save")
    public ResponseEntity<OrderDTO> save(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.save(order));
    }

    @PostMapping("/update/id={order_id}")
    public ResponseEntity<OrderDTO> updateById(@PathVariable("order_id") Long orderId, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateById(orderId, order));
    }

    @DeleteMapping("/delete/id={order_id}")
    public ResponseEntity<Void> deleteById(@PathVariable("order_id") Long orderId) {
        orderService.deleteById(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-all/user-id={user_id}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable("user_id") Long userId) {
        orderService.deleteAllByUserId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
