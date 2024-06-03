package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.PizzaDTOMapper;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PizzaServiceImplementation implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaDTOMapper pizzaDTOMapper;

    public PizzaServiceImplementation(PizzaRepository pizzaRepository, PizzaDTOMapper pizzaDTOMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaDTOMapper = pizzaDTOMapper;
    }

    @Override
    public PizzaDTO save(Pizza pizza) {
        Optional<Pizza> foundPizza = pizzaRepository.findByName(pizza.getName());

        if (foundPizza.isPresent()) {
            throw new ResourceAlreadyExistsException("Pizza " + pizza.getName() + " already exists");
        }

        if (checkIfPizzaIsValid(pizza)) {
            return pizzaDTOMapper.apply(pizzaRepository.save(pizza));
        }

        return null;
    }

    @Override
    public PizzaDTO findById(Long pizzaId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by id " + pizzaId));

        return pizzaDTOMapper.apply(foundPizza);
    }

    @Override
    public PizzaDTO findByName(String name) {
        Pizza foundPizza = pizzaRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by name " + name));

        return pizzaDTOMapper.apply(foundPizza);
    }

    @Override
    public List<PizzaDTO> findAll() {
        List<Pizza> pizzas = pizzaRepository.findAll();

        if (pizzas.isEmpty()) {
            throw new ResourceNotFoundException("No pizzas found");
        }

        return pizzas
                .stream()
                .map(pizzaDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public PizzaDTO updateById(Long pizzaId, Pizza updatedPizza) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by id " + pizzaId));

        Optional<Pizza> foundPizzaByName = pizzaRepository.findByName(updatedPizza.getName());

        if (foundPizzaByName.isPresent()) {
            throw new ResourceAlreadyExistsException("Pizza " + updatedPizza.getName() + " already exists");
        }

        if (checkIfPizzaIsValid(updatedPizza)) {
            foundPizza.setImages(updatedPizza.getImages());
            foundPizza.setName(updatedPizza.getName());
            foundPizza.setIngredients(updatedPizza.getIngredients());
            foundPizza.setPrice(updatedPizza.getPrice());

            return pizzaDTOMapper.apply(pizzaRepository.save(foundPizza));
        }

        return null;
    }

    @Override
    public PizzaDTO updateByName(String name, Pizza updatedPizza) {
        Pizza foundPizza = pizzaRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by name " + name));

        if (checkIfPizzaIsValid(updatedPizza)) {
            foundPizza.setImages(updatedPizza.getImages());
            foundPizza.setIngredients(updatedPizza.getIngredients());
            foundPizza.setPrice(updatedPizza.getPrice());

            return pizzaDTOMapper.apply(pizzaRepository.save(foundPizza));
        }

        return null;
    }

    @Override
    public void deleteById(Long pizzaId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by id " + pizzaId));

        pizzaRepository.delete(foundPizza);
    }

    @Override
    public void deleteByName(String name) {
        Pizza foundPizza = pizzaRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by name " + name));

        pizzaRepository.delete(foundPizza);
    }

    private boolean checkIfPizzaIsValid(Pizza pizza) {
        if (pizza.getName() == null) {
            throw new InvalidArgumentException("The pizza's name can't be null");
        }

        else if (pizza.getName().isEmpty()) {
            throw new InvalidArgumentException("The pizza's name can't be empty");
        }

        else if (pizza.getName().isBlank()) {
            throw new InvalidArgumentException("The pizza's name can't be blank");
        }

        else if (pizza.getPrice() == null) {
            throw new InvalidArgumentException("The pizza's price can't be null");
        }

        else if (pizza.getPrice() <= 0) {
            throw new InvalidArgumentException("The pizza's price must be positive");
        }

        return true;
    }
}
