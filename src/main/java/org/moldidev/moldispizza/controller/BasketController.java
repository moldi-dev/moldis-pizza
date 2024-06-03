package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/baskets")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<BasketDTO>> findAll() {
        return ResponseEntity.ok(basketService.findAll());
    }

    @GetMapping("/find/id={basket_id}")
    public ResponseEntity<BasketDTO> findById(@PathVariable("basket_id") Long basketId) {
        return ResponseEntity.ok(basketService.findById(basketId));
    }

    @GetMapping("/find/user-id={user_id}")
    public ResponseEntity<BasketDTO> findByUserId(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(basketService.findByUserId(userId));
    }

    @GetMapping("/add-pizza/user-id={user_id}/pizza_id={pizza_id}")
    public ResponseEntity<BasketDTO> addPizza(@PathVariable("user_id") Long userId, @PathVariable("pizza_id") Long pizzaId) {
        return ResponseEntity.ok(basketService.addPizzaToUserBasketByPizzaIdAndUserId(pizzaId, userId));
    }

    @GetMapping("/remove-pizza/user-id={user_id}/pizza_id={pizza_id}")
    public ResponseEntity<BasketDTO> removePizza(@PathVariable("user_id") Long userId, @PathVariable("pizza_id") Long pizzaId) {
        return ResponseEntity.ok(basketService.removePizzaFromUserBasketByPizzaIdAndUserId(pizzaId, userId));
    }

    @PostMapping("/save")
    public ResponseEntity<BasketDTO> save(@RequestBody Basket basket) {
        return ResponseEntity.ok(basketService.save(basket));
    }

    @PostMapping("/update/id={basket_id}")
    public ResponseEntity<BasketDTO> updateById(@PathVariable("basket_id") Long basketId, @RequestBody  Basket basket) {
        return ResponseEntity.ok(basketService.updateById(basketId, basket));
    }

    @DeleteMapping("/delete/id={basket_id}")
    public ResponseEntity<Void> deleteById(@PathVariable("basket_id") Long basketId) {
        basketService.deleteById(basketId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/user-id={user_id}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable("user_id") Long userId) {
        basketService.deleteByUserId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
