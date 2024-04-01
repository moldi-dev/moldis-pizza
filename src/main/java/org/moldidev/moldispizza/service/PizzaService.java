package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
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

    public List<Pizza> getAllPizzas() throws ResourceNotFoundException {
        List<Pizza> pizzaList = pizzaRepository.findAll();

        if (!pizzaList.isEmpty()) {
            return pizzaList;
        }

        throw new ResourceNotFoundException("there are no pizzas");
    }

    public Pizza getPizzaById(Long id) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);

        if (foundPizza.isPresent()) {
            return foundPizza.get();
        }

        throw new ResourceNotFoundException("pizza not found by id: " + id);
    }

    public Pizza getPizzaByPizzaName(String name) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(name);

        if (foundPizza.isPresent()) {
            return foundPizza.get();
        }

        throw new ResourceNotFoundException("pizza not found by name: " + name);
    }

    public Pizza addPizza(Pizza pizza) throws ResourceAlreadyExistsException, InvalidArgumentException {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(pizza.getName());

        if (foundPizza.isPresent()) {
            throw new ResourceAlreadyExistsException("pizza with name '" + pizza.getName() + "' already exists");
        }

        else if (pizza.getPrice() <= 0) {
            throw new InvalidArgumentException("pizza's price must be greater than 0");
        }

        return pizzaRepository.save(pizza);
    }

    public Pizza updatePizzaById(Long id, Pizza newPizza) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);

        if (foundPizza.isPresent()) {
            Pizza updatedPizza = foundPizza.get();

            updatedPizza.setName(newPizza.getName());
            updatedPizza.setIngredients(newPizza.getIngredients());
            updatedPizza.setPrice(newPizza.getPrice());

            return pizzaRepository.save(updatedPizza);
        }

        throw new ResourceNotFoundException("pizza not found by id: " + id);
    }

    public Pizza updatePizzaByPizzaName(String pizzaName, Pizza newPizza) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(pizzaName);

        if (foundPizza.isPresent()) {
            Pizza updatedPizza = foundPizza.get();

            updatedPizza.setName(newPizza.getName());
            updatedPizza.setIngredients(newPizza.getIngredients());
            updatedPizza.setPrice(newPizza.getPrice());

            return pizzaRepository.save(updatedPizza);
        }

        throw new ResourceNotFoundException("pizza not found by name: " + pizzaName);
    }

    @Transactional
    public void deletePizzaById(Long id) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);

        if (foundPizza.isPresent()) {
            pizzaRepository.deleteById(id);
        }

        else {
            throw new ResourceNotFoundException("pizza not found by id: " + id);
        }
    }

    @Transactional
    public void deletePizzaByPizzaName(String pizzaName) {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(pizzaName);

        if (foundPizza.isPresent()) {
            pizzaRepository.deletePizzaByName(pizzaName);
        }

        else {
            throw new ResourceNotFoundException("pizza not found by name: " + pizzaName);
        }
    }
}
