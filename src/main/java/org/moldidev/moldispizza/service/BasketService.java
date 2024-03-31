package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final PizzaRepository pizzaRepository;

    public BasketService(BasketRepository basketRepository, PizzaRepository pizzaRepository) {
        this.basketRepository = basketRepository;
        this.pizzaRepository = pizzaRepository;
    }

    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public Optional<Basket> getBasketById(Long id) {
        return basketRepository.findById(id);
    }

    public Optional<Basket> getBasketByUserId(Long id) {
        return basketRepository.getBasketByUserId(id);
    }

    public Optional<Basket> getBasketByUsername(String username) {
        return basketRepository.getBasketByUsername(username);
    }

    public Optional<Double> getBasketTotalPriceByUserId(Long id) {
        return basketRepository.getBasketTotalPriceByUserId(id);
    }

    public Optional<Double> getBasketTotalPriceByUsername(String username) {
        return basketRepository.getBasketTotalPriceByUsername(username);
    }

    public Basket addBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    public Optional<Basket> addPizzaToBasketByBasketIdAndPizzaId(Long basketId, Long pizzaId) {
        Optional<Basket> foundBasket = basketRepository.findById(basketId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isPresent() && foundPizza.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.addPizza(foundPizza.get());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> addPizzaToBasketByUserIdAndPizzaId(Long userId, Long pizzaId) {
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(userId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isPresent() && foundPizza.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.addPizza(foundPizza.get());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> updateBasketById(Long id, Basket newBasket) {
        Optional<Basket> foundBasket = basketRepository.findById(id);

        if (foundBasket.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.setUser(newBasket.getUser());
            updatedBasket.setPizzaList(newBasket.getPizzaList());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> updateBasketByUserId(Long id, Basket newBasket) {
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(id);

        if (foundBasket.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.setUser(newBasket.getUser());
            updatedBasket.setPizzaList(newBasket.getPizzaList());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> updateBasketByUsername(String username, Basket newBasket) {
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);

        if (foundBasket.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.setUser(newBasket.getUser());
            updatedBasket.setPizzaList(newBasket.getPizzaList());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> deletePizzaFromBasketByBasketIdAndPizzaId(Long basketId, Long pizzaId) {
        Optional<Basket> foundBasket = basketRepository.findById(basketId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isPresent() && foundPizza.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.getPizzaList().remove(foundPizza.get());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> deletePizzaFromBasketByUserIdAndPizzaId(Long userId, Long pizzaId) {
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(userId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isPresent() && foundPizza.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.getPizzaList().remove(foundPizza.get());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public Optional<Basket> deletePizzaFromBasketByUsernameAndPizzaId(String username, Long pizzaId) {
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isPresent() && foundPizza.isPresent()) {
            Basket updatedBasket = foundBasket.get();

            updatedBasket.getPizzaList().remove(foundPizza.get());

            return Optional.of(basketRepository.save(updatedBasket));
        }

        return Optional.empty();
    }

    public void deleteBasketById(Long id) {
        basketRepository.deleteById(id);
    }

    @Transactional
    public void deleteBasketByUserId(Long id) {
        basketRepository.deleteBasketByUserId(id);
    }

    @Transactional
    public void deleteBasketByUsername(String username) {
        basketRepository.deleteBasketByUsername(username);
    }
}
