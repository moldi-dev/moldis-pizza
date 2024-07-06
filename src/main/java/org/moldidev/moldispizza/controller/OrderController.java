package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<HTTPResponse> findAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        Page<OrderDTO> result = orderService.findAll(page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("ordersDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/id={id}")
    public ResponseEntity<HTTPResponse> findById(@PathVariable("id") Long orderId) {
        OrderDTO result = orderService.findById(orderId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/user-id={user_id}")
    public ResponseEntity<HTTPResponse> findAllByUserId(@PathVariable("user_id") Long userId, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size, Authentication connectedUser) {
        Page<OrderDTO> result = orderService.findAllByUserId(userId, page.orElse(0), size.orElse(10), connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("ordersDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/user-id={userId}/pizza-id={pizzaId}")
    public ResponseEntity<HTTPResponse> hasUserBoughtThePizza(@PathVariable("userId") Long userId, @PathVariable("pizzaId") Long pizzaId, Authentication connectedUser) {
        Boolean result = orderService.hasUserBoughtThePizza(userId, pizzaId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("answer", result))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HTTPResponse> save(@RequestBody Order order) {
        OrderDTO result = orderService.save(order);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Order created successfully")
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PostMapping("/user-id={user_id}")
    public ResponseEntity<HTTPResponse> placeOrderByUserBasket(@PathVariable("user_id") Long userId, Authentication connectedUser) {
        OrderDTO result = orderService.placeOrderByUserBasket(userId, connectedUser);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Order created successfully")
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PatchMapping("id={id}")
    public ResponseEntity<HTTPResponse> updateById(@PathVariable("id") Long orderId, @RequestBody Order updatedOrder) {
        OrderDTO result = orderService.updateById(orderId, updatedOrder);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Order updated successfully")
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<HTTPResponse> deleteById(@PathVariable("id") Long orderId) {
        orderService.deleteById(orderId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Order deleted successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
