package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/getAllPizzas")
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        List<Pizza> pizzaList = pizzaService.getAllPizzas();

        if (pizzaList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(pizzaList, HttpStatus.OK);
    }

    @GetMapping("/getPizzaById/{id}")
    public ResponseEntity<Pizza> getPizzaById(@PathVariable Long id) {
        Optional<Pizza> pizza = pizzaService.getPizzaById(id);

        return pizza.map(value -> new ResponseEntity<>(value, HttpStatus.OK)
                ).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getPizzaByPizzaName/{pizzaName}")
    public ResponseEntity<Pizza> getPizzaByPizzaName(@PathVariable String pizzaName) {
        Optional<Pizza> pizza = pizzaService.getPizzaByPizzaName(pizzaName);

        return pizza.map(value -> new ResponseEntity<>(value, HttpStatus.OK)
        ).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addPizza")
    public ResponseEntity<Pizza> addPizza(@RequestBody Pizza pizza) {
        Pizza createdPizza = pizzaService.addPizza(pizza);
        return new ResponseEntity<>(createdPizza, HttpStatus.OK);
    }

    @PostMapping("/updatePizzaById/{id}")
    public ResponseEntity<Pizza> updatePizzaById(@PathVariable Long id, @RequestBody Pizza newPizza) {
        Optional<Pizza> updatedPizza = pizzaService.updatePizzaById(id, newPizza);

        return updatedPizza.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/updatePizzaByPizzaName/{pizzaName}")
    public ResponseEntity<Pizza> updatePizzaByPizzaName(@PathVariable String pizzaName, @RequestBody Pizza newPizza) {
        Optional<Pizza> updatedPizza = pizzaService.updatePizzaByPizzaName(pizzaName, newPizza);

        return updatedPizza.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/deletePizzaById/{id}")
    public ResponseEntity<Void> deletePizzaById(@PathVariable Long id) {
        if (pizzaService.getPizzaById(id).isPresent()) {
            pizzaService.deletePizzaById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deletePizzaByPizzaName/{pizzaName}")
    public ResponseEntity<Void> deletePizzaByPizzaName(@PathVariable String pizzaName) {
        if (pizzaService.getPizzaByPizzaName(pizzaName).isPresent()) {
            pizzaService.deletePizzaByPizzaName(pizzaName);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
