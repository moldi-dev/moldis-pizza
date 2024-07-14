package org.moldidev.moldispizza.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.BasketService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping
    public ResponseEntity<HTTPResponse> findAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        Page<BasketDTO> result = basketService.findAll(page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("basketsDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/id={id}")
    public ResponseEntity<HTTPResponse> findById(@PathVariable("id") Long basketId) {
        BasketDTO result = basketService.findById(basketId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("basketDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/user-id={user_id}")
    public ResponseEntity<HTTPResponse> findByUserId(@PathVariable("user_id") Long userId, Authentication connectedUser) {
        BasketDTO result = basketService.findByUserId(userId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("basketDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/add-pizza/user-id={user_id}/pizza-id={pizza_id}")
    public ResponseEntity<HTTPResponse> addPizzaToUserBasket(@PathVariable("user_id") Long userId, @PathVariable("pizza_id") Long pizzaId, Authentication connectedUser) {
        BasketDTO result = basketService.addPizzaToUserBasket(userId, pizzaId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Pizza added successfully in the user's basket")
                        .data(Map.of("basketDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/remove-pizza/user-id={user_id}/pizza-id={pizza_id}")
    public ResponseEntity<HTTPResponse> removePizzaFromUserBasket(@PathVariable("user_id") Long userId, @PathVariable("pizza_id") Long pizzaId, Authentication connectedUser) {
        BasketDTO result = basketService.removePizzaFromUserBasket(userId, pizzaId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Pizza removed successfully from the user's basket")
                        .data(Map.of("basketDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
