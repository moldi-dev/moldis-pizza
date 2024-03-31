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
        List<Basket> basketList = basketService.getAllBaskets();

        if (basketList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/getBasketById/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        Optional<Basket> basket = basketService.getBasketById(id);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getBasketByUserId/{id}")
    public ResponseEntity<Basket> getBasketByUserId(@PathVariable Long id) {
        Optional<Basket> basket = basketService.getBasketByUserId(id);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getBasketByUsername/{username}")
    public ResponseEntity<Basket> getBasketByUsername(@PathVariable String username) {
        Optional<Basket> basket = basketService.getBasketByUsername(username);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getBasketTotalPriceByUserId/{id}")
    public ResponseEntity<Double> getBasketTotalPriceByUserId(@PathVariable Long id) {
        Optional<Double> totalPrice = basketService.getBasketTotalPriceByUserId(id);

        return totalPrice.map(aDouble -> new ResponseEntity<>(aDouble, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getBasketTotalPriceByUsername/{username}")
    public ResponseEntity<Double> getBasketTotalPriceByUsername(@PathVariable String username) {
        Optional<Double> totalPrice = basketService.getBasketTotalPriceByUsername(username);

        return totalPrice.map(aDouble -> new ResponseEntity<>(aDouble, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addBasket")
    public ResponseEntity<Basket> addBasket(@RequestBody Basket basket) {
        Basket createdBasket = basketService.addBasket(basket);
        return new ResponseEntity<>(createdBasket, HttpStatus.OK);
    }

    @PostMapping("/addPizzaToBasketByBasketIdAndPizzaId/{basketId}/{pizzaId}")
    public ResponseEntity<Basket> addPizzaToBasketByBasketIdAndPizzaId(@PathVariable Long basketId, @PathVariable Long pizzaId) {
        Optional<Basket> basket = basketService.addPizzaToBasketByBasketIdAndPizzaId(basketId, pizzaId);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("addPizzaToBasketByUserIdAndPizzaId/{userId}/{pizzaId}")
    public ResponseEntity<Basket> addPizzaToBasketByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId) {
        Optional<Basket> basket = basketService.addPizzaToBasketByUserIdAndPizzaId(userId, pizzaId);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/updateBasketById/{id}")
    public ResponseEntity<Basket> updateBasketById(@PathVariable Long id, @RequestBody Basket newBasket) {
        Optional<Basket> updatedBasket = basketService.updateBasketById(id, newBasket);

        return updatedBasket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/updateBasketByUserId/{id}")
    public ResponseEntity<Basket> updateBasketByUsernameOrUserId(@PathVariable Long id, @RequestBody Basket newBasket) {
        Optional<Basket> updatedBasket = basketService.updateBasketByUserId(id, newBasket);

        return updatedBasket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/deletePizzaFromBasketByBasketIdAndPizzaId/{basketId}/{pizzaId}")
    public ResponseEntity<Basket> deletePizzaFromBasketByBasketIdAndPizzaId(@PathVariable Long basketId, @PathVariable Long pizzaId) {
        Optional<Basket> basket = basketService.deletePizzaFromBasketByBasketIdAndPizzaId(basketId, pizzaId);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/deletePizzaFromBasketByUserIdAndPizzaId/{userId}/{pizzaId}")
    public ResponseEntity<Basket> deletePizzaFromBasketByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId) {
        Optional<Basket> basket = basketService.deletePizzaFromBasketByUserIdAndPizzaId(userId, pizzaId);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/deletePizzaFromBasketByUsernameAndPizzaId/{username}/{pizzaId}")
    public ResponseEntity<Basket> deletePizzaFromBasketByUsernameAndPizzaId(@PathVariable String username, @PathVariable Long pizzaId) {
        Optional<Basket> basket = basketService.deletePizzaFromBasketByUsernameAndPizzaId(username, pizzaId);

        return basket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/updateBasketByUsername/{username}")
    public ResponseEntity<Basket> updateBasketByUsername(@PathVariable String username, @RequestBody Basket newBasket) {
        Optional<Basket> updatedBasket = basketService.updateBasketByUsername(username, newBasket);

        return updatedBasket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/deleteBasketById/{id}")
    public ResponseEntity<Void> deleteBasketById(@PathVariable Long id) {
        if (basketService.getBasketById(id).isPresent()) {
            basketService.deleteBasketById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBasketByUserId/{id}")
    public ResponseEntity<Void> deleteBasketByUserId(@PathVariable Long id) {
        if (basketService.getBasketByUserId(id).isPresent()) {
            basketService.deleteBasketByUserId(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBasketByUsername/{username}")
    public ResponseEntity<Void> deleteBasketByUsername(@PathVariable String username) {
        if (basketService.getBasketByUsername(username).isPresent()) {
            basketService.deleteBasketByUsername(username);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
