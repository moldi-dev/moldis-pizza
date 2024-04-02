package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.dto.PizzaDTOMapper;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaDTOMapper pizzaDTOMapper;

    public PizzaService(PizzaRepository pizzaRepository, PizzaDTOMapper pizzaDTOMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaDTOMapper = pizzaDTOMapper;
    }

    public List<PizzaDTO> getAllPizzas() throws ResourceNotFoundException {
        List<Pizza> pizzaList = pizzaRepository.findAll();

        if (!pizzaList.isEmpty()) {
            return pizzaList.stream().map(pizzaDTOMapper).collect(Collectors.toList());
        }

        throw new ResourceNotFoundException("there are no pizzas");
    }

    public PizzaDTO getPizzaById(Long id) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);

        if (foundPizza.isPresent()) {
            return pizzaDTOMapper.apply(foundPizza.get());
        }

        throw new ResourceNotFoundException("pizza not found by id: " + id);
    }

    public PizzaDTO getPizzaByPizzaName(String name) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(name);

        if (foundPizza.isPresent()) {
            return pizzaDTOMapper.apply(foundPizza.get());
        }

        throw new ResourceNotFoundException("pizza not found by name: " + name);
    }

    public PizzaDTO addPizza(Pizza pizza) throws ResourceAlreadyExistsException, InvalidArgumentException {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(pizza.getName());

        if (foundPizza.isPresent()) {
            throw new ResourceAlreadyExistsException("pizza with name '" + pizza.getName() + "' already exists");
        }

        else if (pizza.getName().length() > 100) {
            throw new InvalidArgumentException("pizza's name must be at most 100 characters long");
        }

        else if (pizza.getPrice() <= 0) {
            throw new InvalidArgumentException("pizza's price must be greater than 0");
        }

        Pizza savedPizza = pizzaRepository.save(pizza);

        return pizzaDTOMapper.apply(savedPizza);
    }

    public PizzaDTO updatePizzaById(Long id, Pizza newPizza) throws ResourceNotFoundException, InvalidArgumentException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);

        if (foundPizza.isPresent()) {
            Pizza updatedPizza = foundPizza.get();

            if (newPizza.getName().length() > 100) {
                throw new InvalidArgumentException("pizza's name must be at most 100 characters long");
            }

            else if (newPizza.getPrice() <= 0) {
                throw new InvalidArgumentException("pizza's price must be greater than 0");
            }

            updatedPizza.setName(newPizza.getName());
            updatedPizza.setIngredients(newPizza.getIngredients());
            updatedPizza.setPrice(newPizza.getPrice());

            Pizza savedPizza = pizzaRepository.save(updatedPizza);

            return pizzaDTOMapper.apply(savedPizza);
        }

        throw new ResourceNotFoundException("pizza not found by id: " + id);
    }

    public PizzaDTO updatePizzaByPizzaName(String pizzaName, Pizza newPizza) throws ResourceNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findPizzaByName(pizzaName);

        if (foundPizza.isPresent()) {
            Pizza updatedPizza = foundPizza.get();

            if (newPizza.getName().length() > 100) {
                throw new InvalidArgumentException("pizza's name must be at most 100 characters long");
            }

            else if (newPizza.getPrice() <= 0) {
                throw new InvalidArgumentException("pizza's price must be greater than 0");
            }

            updatedPizza.setName(newPizza.getName());
            updatedPizza.setIngredients(newPizza.getIngredients());
            updatedPizza.setPrice(newPizza.getPrice());

            Pizza savedPizza = pizzaRepository.save(updatedPizza);

            return pizzaDTOMapper.apply(savedPizza);
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
