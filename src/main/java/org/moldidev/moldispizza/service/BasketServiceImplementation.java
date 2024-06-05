package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.InvalidInputException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.BasketDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BasketServiceImplementation implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketDTOMapper basketDTOMapper;
    private final UserRepository userRepository;
    private final PizzaRepository pizzaRepository;

    public BasketServiceImplementation(BasketRepository basketRepository, BasketDTOMapper basketDTOMapper, UserRepository userRepository, PizzaRepository pizzaRepository) {
        this.basketRepository = basketRepository;
        this.basketDTOMapper = basketDTOMapper;
        this.userRepository = userRepository;
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public BasketDTO save(Basket basket) {
        checkIfBasketIsValid(basket);

        return basketDTOMapper.apply(basketRepository.save(basket));
    }

    @Override
    public BasketDTO addPizzaToUserBasketByPizzaIdAndUserId(Long pizzaId, Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by id " + userId));

        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by id " + pizzaId));

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by user id " + userId));

        foundBasket.addPizza(foundPizza);
        foundBasket.setTotalPrice(foundBasket.getTotalPrice() + foundPizza.getPrice());

        return basketDTOMapper.apply(basketRepository.save(foundBasket));
    }

    @Override
    public BasketDTO findById(Long basketId) {
        Basket foundBasket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by id " + basketId));

        return basketDTOMapper.apply(foundBasket);
    }

    @Override
    public BasketDTO findByUserId(Long userId) {
        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by user id " + userId));

        return basketDTOMapper.apply(foundBasket);
    }

    @Override
    public List<BasketDTO> findAll() {
        List<Basket> baskets = basketRepository.findAll();

        if (baskets.isEmpty()) {
            throw new ResourceNotFoundException("No baskets found");
        }

        return baskets
                .stream()
                .map(basketDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public BasketDTO updateById(Long basketId, Basket updatedBasket) {
        Basket foundBasket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by id " + basketId));

        checkIfBasketIsValid(updatedBasket);

        foundBasket.setPizzas(updatedBasket.getPizzas());
        foundBasket.setUser(updatedBasket.getUser());
        foundBasket.setTotalPrice(updatedBasket.getTotalPrice());

        return basketDTOMapper.apply(basketRepository.save(foundBasket));
    }

    @Override
    public BasketDTO removePizzaFromUserBasketByPizzaIdAndUserId(Long pizzaId, Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by id " + userId));

        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found by id " + pizzaId));

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by user id " + userId));

        if (foundBasket.getPizzas().contains(foundPizza)) {
            foundBasket.removePizza(foundPizza);
            foundBasket.setTotalPrice(foundBasket.getTotalPrice() - foundPizza.getPrice());

            return basketDTOMapper.apply(basketRepository.save(foundBasket));
        }

        throw new ResourceNotFoundException("Pizza not found in user's " + foundUser.getUsername() + "'s basket");
    }

    @Override
    public void deleteById(Long basketId) {
        Basket foundBasket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by id " + basketId));

        basketRepository.delete(foundBasket);
    }

    @Override
    public void deleteByUserId(Long userId) {
        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by user id " + userId));

        basketRepository.delete(foundBasket);
    }

    private void checkIfBasketIsValid(Basket basket) {
        if (basket.getUser() == null) {
            throw new InvalidInputException("The user can't be null");
        }

        else if (basket.getTotalPrice() == null) {
            throw new InvalidInputException("The total price can't be null");
        }

        else if (basket.getTotalPrice() < 0) {
            throw new InvalidInputException("The total price must be positive");
        }

        else if (basket.getPizzas() == null) {
            throw new InvalidInputException("The pizza list can't be null");
        }
    }
}
