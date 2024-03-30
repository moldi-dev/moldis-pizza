package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    public Optional<Pizza> getPizzaById(Long id) {
        return pizzaRepository.findById(id);
    }

    public Optional<Pizza> getPizzaByPizzaName(String name) {
        return pizzaRepository.findPizzaByName(name);
    }

    public Pizza addPizza(Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    public Optional<Pizza> updatePizzaById(Long id, Pizza newPizza) {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);

        if (foundPizza.isPresent()) {
            Pizza updatedPizza = foundPizza.get();

            updatedPizza.setName(newPizza.getName());
            updatedPizza.setIngredients(newPizza.getIngredients());
            updatedPizza.setPrice(newPizza.getPrice());

            return Optional.of(pizzaRepository.save(updatedPizza));
        }

        return Optional.empty();
    }

    public Optional<Pizza> updatePizzaByPizzaName(String pizzaName, Pizza newPizza) {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(pizzaName);

        if (foundPizza.isPresent()) {
            Pizza updatedPizza = foundPizza.get();

            updatedPizza.setName(newPizza.getName());
            updatedPizza.setIngredients(newPizza.getIngredients());
            updatedPizza.setPrice(newPizza.getPrice());

            return Optional.of(pizzaRepository.save(updatedPizza));
        }

        return Optional.empty();
    }

    public void deletePizzaById(Long id) {
        pizzaRepository.deleteById(id);
    }

    @Transactional
    public void deletePizzaByPizzaName(String pizzaName) {
        pizzaRepository.deletePizzaByName(pizzaName);
    }
}
