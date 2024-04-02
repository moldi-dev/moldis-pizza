package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("")
    public ResponseEntity<List<PizzaDTO>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }

    @GetMapping("/get-pizza-by-id/{id}")
    public ResponseEntity<PizzaDTO> getPizzaById(@PathVariable Long id) {
        return ResponseEntity.ok(pizzaService.getPizzaById(id));
    }

    @GetMapping("/get-pizza-by-name/{pizzaName}")
    public ResponseEntity<PizzaDTO> getPizzaByPizzaName(@PathVariable String pizzaName) {
        return ResponseEntity.ok(pizzaService.getPizzaByPizzaName(pizzaName));
    }

    @PostMapping("/add-pizza")
    public ResponseEntity<PizzaDTO> addPizza(@RequestBody Pizza pizza) {
        return ResponseEntity.ok(pizzaService.addPizza(pizza));
    }

    @PostMapping("/update-pizza-by-id/{id}")
    public ResponseEntity<PizzaDTO> updatePizzaById(@PathVariable Long id, @RequestBody Pizza newPizza) {
        return ResponseEntity.ok(pizzaService.updatePizzaById(id, newPizza));
    }

    @PostMapping("/update-pizza-by-name/{pizzaName}")
    public ResponseEntity<PizzaDTO> updatePizzaByPizzaName(@PathVariable String pizzaName, @RequestBody Pizza newPizza) {
       return ResponseEntity.ok(pizzaService.updatePizzaByPizzaName(pizzaName, newPizza));
    }

    @DeleteMapping("/delete-pizza-by-id/{id}")
    public ResponseEntity<Void> deletePizzaById(@PathVariable Long id) {
        pizzaService.deletePizzaById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-pizza-by-name/{pizzaName}")
    public ResponseEntity<Void> deletePizzaByPizzaName(@PathVariable String pizzaName) {
        pizzaService.deletePizzaByPizzaName(pizzaName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
