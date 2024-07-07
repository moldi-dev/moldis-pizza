package org.moldidev.moldispizza.controller;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.OrderService;
import org.moldidev.moldispizza.service.PaymentService;
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
    private final PaymentService paymentService;

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

    @GetMapping("/exists/user-id={userId}/pizza-id={pizzaId}")
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
    public ResponseEntity<HTTPResponse> save(@RequestBody Order order) throws StripeException {
        OrderDTO result = orderService.save(order);
        String paymentLink = paymentService.createPaymentLink(result);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Order created successfully")
                        .developerMessage(paymentLink)
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PostMapping("/user-id={user_id}")
    public ResponseEntity<HTTPResponse> placeOrderByUserBasket(@PathVariable("user_id") Long userId, Authentication connectedUser) throws StripeException {
        OrderDTO result = orderService.placeOrderByUserBasket(userId, connectedUser);
        String paymentLink = paymentService.createPaymentLink(result);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .developerMessage(paymentLink)
                        .message("Order created successfully")
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PostMapping("/pay-pending-order/id={orderId}")
    public ResponseEntity<HTTPResponse> payPendingOrder(@PathVariable("orderId") Long orderId, Authentication connectedUser) throws StripeException {
        OrderDTO result = orderService.findById(orderId, connectedUser);
        String paymentLink = paymentService.createPaymentLink(result);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .developerMessage(paymentLink)
                        .build()
        );
    }

    @PatchMapping("/set-paid/id={id}")
    public ResponseEntity<HTTPResponse> setOrderAsPaid(@PathVariable("id") Long orderId, Authentication connectedUser) {
        OrderDTO result = orderService.setOrderAsPaid(orderId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("The order has been paid successfully!")
                        .data(Map.of("orderDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/id={id}")
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
