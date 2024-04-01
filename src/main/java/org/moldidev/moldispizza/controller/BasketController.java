package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/getAllBaskets")
    public ResponseEntity<List<Basket>> getAllBaskets() {
        return ResponseEntity.ok(basketService.getAllBaskets());
    }

    @GetMapping("/getBasketById/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketById(id));
    }

    @GetMapping("/getBasketByUserId/{id}")
    public ResponseEntity<Basket> getBasketByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketByUserId(id));
    }

    @GetMapping("/getBasketByUsername/{username}")
    public ResponseEntity<Basket> getBasketByUsername(@PathVariable String username) {
        return ResponseEntity.ok(basketService.getBasketByUsername(username));
    }

    @GetMapping("/getBasketTotalPriceByUserId/{id}")
    public ResponseEntity<Double> getBasketTotalPriceByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketTotalPriceByUserId(id));
    }

    @GetMapping("/getBasketTotalPriceByUsername/{username}")
    public ResponseEntity<Double> getBasketTotalPriceByUsername(@PathVariable String username) {
        return ResponseEntity.ok(basketService.getBasketTotalPriceByUsername(username));
    }

    @PostMapping("/addBasket")
    public ResponseEntity<Basket> addBasket(@RequestBody Basket basket) {
        return ResponseEntity.ok(basketService.addBasket(basket));
    }

    @PostMapping("/addPizzaToBasketByBasketIdAndPizzaId/{basketId}/{pizzaId}")
    public ResponseEntity<Basket> addPizzaToBasketByBasketIdAndPizzaId(@PathVariable Long basketId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.addPizzaToBasketByBasketIdAndPizzaId(basketId, pizzaId));
    }

    @PostMapping("addPizzaToBasketByUserIdAndPizzaId/{userId}/{pizzaId}")
    public ResponseEntity<Basket> addPizzaToBasketByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.addPizzaToBasketByUserIdAndPizzaId(userId, pizzaId));
    }

    @PostMapping("/updateBasketById/{id}")
    public ResponseEntity<Basket> updateBasketById(@PathVariable Long id, @RequestBody Basket newBasket) {
        return ResponseEntity.ok(basketService.updateBasketById(id, newBasket));
    }

    @PostMapping("/updateBasketByUserId/{id}")
    public ResponseEntity<Basket> updateBasketByUserId(@PathVariable Long id, @RequestBody Basket newBasket) {
        return ResponseEntity.ok(basketService.updateBasketByUserId(id, newBasket));
    }

    @PostMapping("/updateBasketByUsername/{username}")
    public ResponseEntity<Basket> updateBasketByUsername(@PathVariable String username, @RequestBody Basket newBasket) {
        return ResponseEntity.ok(basketService.updateBasketByUsername(username, newBasket));
    }

    @PostMapping("/deletePizzaFromBasketByBasketIdAndPizzaId/{basketId}/{pizzaId}")
    public ResponseEntity<Basket> deletePizzaFromBasketByBasketIdAndPizzaId(@PathVariable Long basketId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.deletePizzaFromBasketByBasketIdAndPizzaId(basketId, pizzaId));
    }

    @PostMapping("/deletePizzaFromBasketByUserIdAndPizzaId/{userId}/{pizzaId}")
    public ResponseEntity<Basket> deletePizzaFromBasketByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.deletePizzaFromBasketByUserIdAndPizzaId(userId, pizzaId));
    }

    @PostMapping("/deletePizzaFromBasketByUsernameAndPizzaId/{username}/{pizzaId}")
    public ResponseEntity<Basket> deletePizzaFromBasketByUsernameAndPizzaId(@PathVariable String username, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.deletePizzaFromBasketByUsernameAndPizzaId(username, pizzaId));
    }

    @DeleteMapping("/deleteBasketById/{id}")
    public ResponseEntity<Void> deleteBasketById(@PathVariable Long id) {
        basketService.deleteBasketById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteBasketByUserId/{id}")
    public ResponseEntity<Void> deleteBasketByUserId(@PathVariable Long id) {
        basketService.deleteBasketByUserId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteBasketByUsername/{username}")
    public ResponseEntity<Void> deleteBasketByUsername(@PathVariable String username) {
        basketService.deleteBasketByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
