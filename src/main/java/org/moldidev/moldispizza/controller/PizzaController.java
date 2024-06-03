package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<PizzaDTO>> findAll() {
        return ResponseEntity.ok(pizzaService.findAll());
    }

    @GetMapping("/find/id={pizza_id}")
    public ResponseEntity<PizzaDTO> findById(@PathVariable("pizza_id") Long pizza_id) {
        return ResponseEntity.ok(pizzaService.findById(pizza_id));
    }

    @GetMapping("/find/name={name}")
    public ResponseEntity<PizzaDTO> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(pizzaService.findByName(name));
    }

    @PostMapping("/save")
    public ResponseEntity<PizzaDTO> save(@RequestBody Pizza pizza) {
        return ResponseEntity.ok(pizzaService.save(pizza));
    }

    @PostMapping("/update/id={pizza_id}")
    public ResponseEntity<PizzaDTO> updateById(@PathVariable("pizza_id") Long pizzaId, @RequestBody Pizza pizza) {
        return ResponseEntity.ok(pizzaService.updateById(pizzaId, pizza));
    }

    @PostMapping("/update/name={name}")
    public ResponseEntity<PizzaDTO> updateByName(@PathVariable("name") String name, @RequestBody Pizza pizza) {
        return ResponseEntity.ok(pizzaService.updateByName(name, pizza));
    }

    @DeleteMapping("/delete/id={pizza_id}")
    public ResponseEntity<Void> deleteById(@PathVariable("pizza_id") Long pizzaId) {
        pizzaService.deleteById(pizzaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/name={name}")
    public ResponseEntity<Void> deleteByName(@PathVariable("name") String name) {
        pizzaService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
