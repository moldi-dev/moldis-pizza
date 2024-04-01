package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final PizzaRepository pizzaRepository;
    private final UserRepository userRepository;

    public BasketService(BasketRepository basketRepository, PizzaRepository pizzaRepository, UserRepository userRepository) {
        this.basketRepository = basketRepository;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
    }

    public List<Basket> getAllBaskets() throws ResourceNotFoundException {
        List<Basket> basketList = basketRepository.findAll();

        if (!basketList.isEmpty()) {
            return basketList;
        }

        throw new ResourceNotFoundException("there are no baskets");
    }

    public Basket getBasketById(Long id) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(id);

        if (foundBasket.isPresent()) {
            return foundBasket.get();
        }

        throw new ResourceNotFoundException("basket not found by id: " + id);
    }

    public Basket getBasketByUserId(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + id);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + id);
        }

        return foundBasket.get();
    }

    public Basket getBasketByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        return foundBasket.get();
    }

    public Double getBasketTotalPriceByUserId(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(id);
        Optional<Double> foundPrice = basketRepository.getBasketTotalPriceByUserId(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + id);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + id);
        }

        else if (foundPrice.isEmpty()) {
            throw new ResourceNotFoundException("basket total price not found by user id: " + id);
        }

        return foundPrice.get();
    }

    public Double getBasketTotalPriceByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);
        Optional<Double> foundPrice = basketRepository.getBasketTotalPriceByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        else if (foundPrice.isEmpty()) {
            throw new ResourceNotFoundException("basket total price not found by username: " + username);
        }

        return foundPrice.get();
    }

    public Basket addBasket(Basket basket) throws ResourceAlreadyExistsException {
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(basket.getUser().getUsername());

        if (foundBasket.isPresent()) {
            throw new ResourceNotFoundException(basket.getUser().getUsername() + " already has a basket");
        }

        return basketRepository.save(basket);
    }

    public Basket addPizzaToBasketByBasketIdAndPizzaId(Long basketId, Long pizzaId) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(basketId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by id: " + basketId);
        }

        else if (foundPizza.isEmpty()) {
            throw new ResourceNotFoundException("pizza not found by id: " + pizzaId);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.addPizza(foundPizza.get());

        return basketRepository.save(updatedBasket);

    }

    public Basket addPizzaToBasketByUserIdAndPizzaId(Long userId, Long pizzaId) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(userId);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(userId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + userId);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + userId);
        }

        else if (foundPizza.isEmpty()) {
            throw new ResourceNotFoundException("pizza not found by id: " + pizzaId);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.addPizza(foundPizza.get());

        return basketRepository.save(updatedBasket);
    }

    public Basket updateBasketById(Long id, Basket newBasket) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(id);

        if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by id: " + id);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.setUser(newBasket.getUser());
        updatedBasket.setPizzaList(newBasket.getPizzaList());

        return basketRepository.save(updatedBasket);

    }

    public Basket updateBasketByUserId(Long id, Basket newBasket) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + id);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + id);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.setUser(newBasket.getUser());
        updatedBasket.setPizzaList(newBasket.getPizzaList());

        return basketRepository.save(updatedBasket);

    }

    public Basket updateBasketByUsername(String username, Basket newBasket) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.setUser(newBasket.getUser());
        updatedBasket.setPizzaList(newBasket.getPizzaList());

        return basketRepository.save(updatedBasket);
    }

    public Basket deletePizzaFromBasketByBasketIdAndPizzaId(Long basketId, Long pizzaId) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(basketId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by id: " + basketId);
        }

        else if (foundPizza.isEmpty()) {
            throw new ResourceNotFoundException("pizza not found by id: " + pizzaId);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.getPizzaList().remove(foundPizza.get());

        return basketRepository.save(updatedBasket);

    }

    public Basket deletePizzaFromBasketByUserIdAndPizzaId(Long userId, Long pizzaId) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(userId);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(userId);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + userId);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + userId);
        }

        else if (foundPizza.isEmpty()) {
            throw new ResourceNotFoundException("pizza not found by id: " + pizzaId);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.getPizzaList().remove(foundPizza.get());

        return basketRepository.save(updatedBasket);
    }

    public Basket deletePizzaFromBasketByUsernameAndPizzaId(String username, Long pizzaId) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        else if (foundPizza.isEmpty()) {
            throw new ResourceNotFoundException("pizza not found by id: " + pizzaId);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.getPizzaList().remove(foundPizza.get());

        return basketRepository.save(updatedBasket);
    }

    @Transactional
    public void deleteBasketById(Long id) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(id);

        if (foundBasket.isPresent()) {
            basketRepository.deleteById(id);
        }

        else {
            throw new ResourceNotFoundException("basket not found by id: " + id);
        }
    }

    @Transactional
    public void deleteBasketByUserId(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + id);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + id);
        }

        basketRepository.deleteBasketByUserId(id);
    }

    @Transactional
    public void deleteBasketByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        basketRepository.deleteBasketByUsername(username);
    }
}
