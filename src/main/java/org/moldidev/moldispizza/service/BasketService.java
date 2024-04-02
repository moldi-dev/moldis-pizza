package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.dto.BasketDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final PizzaRepository pizzaRepository;
    private final UserRepository userRepository;
    private final BasketDTOMapper basketDTOMapper;

    public BasketService(BasketRepository basketRepository, PizzaRepository pizzaRepository, UserRepository userRepository, BasketDTOMapper basketDTOMapper) {
        this.basketRepository = basketRepository;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
        this.basketDTOMapper = basketDTOMapper;
    }

    public List<BasketDTO> getAllBaskets() throws ResourceNotFoundException {
        List<Basket> basketList = basketRepository.findAll();

        if (!basketList.isEmpty()) {
            return basketList.stream().map(basketDTOMapper).collect(Collectors.toList());
        }

        throw new ResourceNotFoundException("there are no baskets");
    }

    public BasketDTO getBasketById(Long id) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(id);

        if (foundBasket.isPresent()) {
            return basketDTOMapper.apply(foundBasket.get());
        }

        throw new ResourceNotFoundException("basket not found by id: " + id);
    }

    public BasketDTO getBasketByUserId(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + id);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by user id: " + id);
        }

        return basketDTOMapper.apply(foundBasket.get());
    }

    public BasketDTO getBasketByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        Optional<Basket> foundBasket = basketRepository.getBasketByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }

        else if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by username: " + username);
        }

        return basketDTOMapper.apply(foundBasket.get());
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

    public BasketDTO addBasket(Basket basket) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(basket.getUser().getId());
        Optional<Basket> foundBasket = basketRepository.getBasketByUserId(basket.getUser().getId());

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("user not found by id: " + basket.getUser().getId());
        }

        else if (foundBasket.isPresent()) {
            throw new ResourceAlreadyExistsException(foundUser.get().getUsername() + " already has a basket");
        }

        Basket savedBasket = basketRepository.save(basket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO addPizzaToBasketByBasketIdAndPizzaId(Long basketId, Long pizzaId) throws ResourceNotFoundException {
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

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO addPizzaToBasketByUserIdAndPizzaId(Long userId, Long pizzaId) throws ResourceNotFoundException {
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

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO updateBasketById(Long id, Basket newBasket) throws ResourceNotFoundException {
        Optional<Basket> foundBasket = basketRepository.findById(id);

        if (foundBasket.isEmpty()) {
            throw new ResourceNotFoundException("basket not found by id: " + id);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.setUser(newBasket.getUser());
        updatedBasket.setPizzaList(newBasket.getPizzaList());

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO updateBasketByUserId(Long id, Basket newBasket) throws ResourceNotFoundException {
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

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO updateBasketByUsername(String username, Basket newBasket) throws ResourceNotFoundException {
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

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO deletePizzaFromBasketByBasketIdAndPizzaId(Long basketId, Long pizzaId) throws ResourceNotFoundException {
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

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO deletePizzaFromBasketByUserIdAndPizzaId(Long userId, Long pizzaId) throws ResourceNotFoundException {
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

        else if (!isPizzaInBasket(foundBasket.get(), foundPizza.get())) {
            throw new ResourceNotFoundException("pizza with id " + pizzaId + " is not in the basket owned by user with id " + userId);
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.getPizzaList().remove(foundPizza.get());

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
    }

    public BasketDTO deletePizzaFromBasketByUsernameAndPizzaId(String username, Long pizzaId) throws ResourceNotFoundException {
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

        else if (!isPizzaInBasket(foundBasket.get(), foundPizza.get())) {
            throw new ResourceNotFoundException("pizza with id " + pizzaId + " is not in the basket owned by user '" + username + "'");
        }

        Basket updatedBasket = foundBasket.get();

        updatedBasket.getPizzaList().remove(foundPizza.get());

        Basket savedBasket = basketRepository.save(updatedBasket);

        return basketDTOMapper.apply(savedBasket);
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

    private boolean isPizzaInBasket(Basket basket, Pizza pizza) {
        for (Pizza currentPizza : basket.getPizzaList()) {
            if (currentPizza.getName().equals(pizza.getName())) {
                return true;
            }
        }

        return false;
    }
}
