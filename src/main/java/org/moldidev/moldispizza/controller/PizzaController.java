package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/getAllPizzas")
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }

    @GetMapping("/getPizzaById/{id}")
    public ResponseEntity<Pizza> getPizzaById(@PathVariable Long id) {
        return ResponseEntity.ok(pizzaService.getPizzaById(id));
    }

    @GetMapping("/getPizzaByPizzaName/{pizzaName}")
    public ResponseEntity<Pizza> getPizzaByPizzaName(@PathVariable String pizzaName) {
        return ResponseEntity.ok(pizzaService.getPizzaByPizzaName(pizzaName));
    }

    @PostMapping("/addPizza")
    public ResponseEntity<Pizza> addPizza(@RequestBody Pizza pizza) {
        return ResponseEntity.ok(pizzaService.addPizza(pizza));
    }

    @PostMapping("/updatePizzaById/{id}")
    public ResponseEntity<Pizza> updatePizzaById(@PathVariable Long id, @RequestBody Pizza newPizza) {
        return ResponseEntity.ok(pizzaService.updatePizzaById(id, newPizza));
    }

    @PostMapping("/updatePizzaByPizzaName/{pizzaName}")
    public ResponseEntity<Pizza> updatePizzaByPizzaName(@PathVariable String pizzaName, @RequestBody Pizza newPizza) {
       return ResponseEntity.ok(pizzaService.updatePizzaByPizzaName(pizzaName, newPizza));
    }

    @DeleteMapping("/deletePizzaById/{id}")
    public ResponseEntity<Void> deletePizzaById(@PathVariable Long id) {
        pizzaService.deletePizzaById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deletePizzaByPizzaName/{pizzaName}")
    public ResponseEntity<Void> deletePizzaByPizzaName(@PathVariable String pizzaName) {
        pizzaService.deletePizzaByPizzaName(pizzaName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
