package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.BasketService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<HTTPResponse> findByUserId(@PathVariable("user_id") Long userId) {
        BasketDTO result = basketService.findByUserId(userId);

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

    @PostMapping
    public ResponseEntity<HTTPResponse> save(@RequestBody Basket basket) {
        BasketDTO result = basketService.save(basket);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Basket created successfully")
                        .data(Map.of("basketDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PatchMapping("/id={id}")
    public ResponseEntity<HTTPResponse> updateById(@PathVariable("id") Long basketId, @RequestBody Basket updatedBasket) {
        BasketDTO result = basketService.updateById(basketId, updatedBasket);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Basket updated successfully")
                        .data(Map.of("basketDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/add/user-id={user_id}/pizza-id={pizza_id}")
    public ResponseEntity<HTTPResponse> addPizzaToUserBasket(@PathVariable("user_id") Long userId, @PathVariable("pizza_id") Long pizzaId) {
        BasketDTO result = basketService.addPizzaToUserBasket(userId, pizzaId);

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

    @PatchMapping("/remove/user-id={user_id}/pizza_id={pizza_id}")
    public ResponseEntity<HTTPResponse> removePizzaFromUserBasket(@PathVariable("user_id") Long userId, @PathVariable("pizza_id") Long pizzaId) {
        BasketDTO result = basketService.removePizzaFromUserBasket(userId, pizzaId);

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

    @DeleteMapping("/id={id}")
    public ResponseEntity<HTTPResponse> deleteById(@PathVariable("id") Long basketId) {
        basketService.deleteById(basketId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Basket deleted successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
