package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baskets")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("")
    public ResponseEntity<List<BasketDTO>> getAllBaskets() {
        return ResponseEntity.ok(basketService.getAllBaskets());
    }

    @GetMapping("/get-basket-by-id/{id}")
    public ResponseEntity<BasketDTO> getBasketById(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketById(id));
    }

    @GetMapping("/get-basket-by-user-id/{id}")
    public ResponseEntity<BasketDTO> getBasketByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketByUserId(id));
    }

    @GetMapping("/get-basket-by-username/{username}")
    public ResponseEntity<BasketDTO> getBasketByUsername(@PathVariable String username) {
        return ResponseEntity.ok(basketService.getBasketByUsername(username));
    }

    @GetMapping("/get-basket-total-price-by-user-id/{id}")
    public ResponseEntity<Double> getBasketTotalPriceByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketTotalPriceByUserId(id));
    }

    @GetMapping("/get-basket-total-price-by-username/{username}")
    public ResponseEntity<Double> getBasketTotalPriceByUsername(@PathVariable String username) {
        return ResponseEntity.ok(basketService.getBasketTotalPriceByUsername(username));
    }

    @PostMapping("/add-basket")
    public ResponseEntity<BasketDTO> addBasket(@RequestBody Basket basket) {
        return ResponseEntity.ok(basketService.addBasket(basket));
    }

    @PostMapping("/add-pizza-to-basket-by-basket-id-and-pizza-id/{basketId}/{pizzaId}")
    public ResponseEntity<BasketDTO> addPizzaToBasketByBasketIdAndPizzaId(@PathVariable Long basketId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.addPizzaToBasketByBasketIdAndPizzaId(basketId, pizzaId));
    }

    @PostMapping("add-pizza-to-basket-by-user-id-and-pizza-id/{userId}/{pizzaId}")
    public ResponseEntity<BasketDTO> addPizzaToBasketByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.addPizzaToBasketByUserIdAndPizzaId(userId, pizzaId));
    }

    @PostMapping("/update-basket-by-id/{id}")
    public ResponseEntity<BasketDTO> updateBasketById(@PathVariable Long id, @RequestBody Basket newBasket) {
        return ResponseEntity.ok(basketService.updateBasketById(id, newBasket));
    }

    @PostMapping("/update-basket-by-user-id/{id}")
    public ResponseEntity<BasketDTO> updateBasketByUserId(@PathVariable Long id, @RequestBody Basket newBasket) {
        return ResponseEntity.ok(basketService.updateBasketByUserId(id, newBasket));
    }

    @PostMapping("/update-basket-by-username/{username}")
    public ResponseEntity<BasketDTO> updateBasketByUsername(@PathVariable String username, @RequestBody Basket newBasket) {
        return ResponseEntity.ok(basketService.updateBasketByUsername(username, newBasket));
    }

    @PostMapping("/delete-pizza-from-basket-by-basket-id-and-pizza-id/{basketId}/{pizzaId}")
    public ResponseEntity<BasketDTO> deletePizzaFromBasketByBasketIdAndPizzaId(@PathVariable Long basketId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.deletePizzaFromBasketByBasketIdAndPizzaId(basketId, pizzaId));
    }

    @PostMapping("/delete-pizza-from-basket-by-user-id-and-pizza-id/{userId}/{pizzaId}")
    public ResponseEntity<BasketDTO> deletePizzaFromBasketByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.deletePizzaFromBasketByUserIdAndPizzaId(userId, pizzaId));
    }

    @PostMapping("/delete-pizza-from-basket-by-username-and-pizza-id/{username}/{pizzaId}")
    public ResponseEntity<BasketDTO> deletePizzaFromBasketByUsernameAndPizzaId(@PathVariable String username, @PathVariable Long pizzaId) {
        return ResponseEntity.ok(basketService.deletePizzaFromBasketByUsernameAndPizzaId(username, pizzaId));
    }

    @DeleteMapping("/delete-basket-by-id/{id}")
    public ResponseEntity<Void> deleteBasketById(@PathVariable Long id) {
        basketService.deleteBasketById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-basket-by-user-id/{id}")
    public ResponseEntity<Void> deleteBasketByUserId(@PathVariable Long id) {
        basketService.deleteBasketByUserId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-basket-by-username/{username}")
    public ResponseEntity<Void> deleteBasketByUsername(@PathVariable String username) {
        basketService.deleteBasketByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
