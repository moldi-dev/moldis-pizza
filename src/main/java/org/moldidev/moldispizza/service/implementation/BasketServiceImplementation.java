package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.BasketDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.moldidev.moldispizza.service.BasketService;
import org.moldidev.moldispizza.service.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasketServiceImplementation implements BasketService {

    private final BasketRepository basketRepository;
    private final BasketDTOMapper basketDTOMapper;
    private final PizzaRepository pizzaRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;

    @Override
    public BasketDTO findById(Long basketId) {
        Basket foundBasket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("The basket by the provided id doesn't exist"));

        return basketDTOMapper.apply(foundBasket);
    }

    @Override
    public BasketDTO findByUserId(Long userId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The basket by the provided user id doesn't exist"));

        return basketDTOMapper.apply(foundBasket);
    }

    @Override
    public Page<BasketDTO> findAll(int page, int size) {
        Page<Basket> baskets = basketRepository.findAll(PageRequest.of(page, size));

        if (baskets.isEmpty()) {
            throw new ResourceNotFoundException("No baskets exist");
        }

        return baskets.map(basketDTOMapper);
    }

    @Override
    public BasketDTO addPizzaToUserBasket(Long userId, Long pizzaId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The basket by the provided user id doesn't exist"));

        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        foundBasket.getPizzas().add(foundPizza);
        foundBasket.setTotalPrice(foundBasket.getTotalPrice() + foundPizza.getPrice());

        return basketDTOMapper.apply(basketRepository.save(foundBasket));
    }

    @Override
    public BasketDTO removePizzaFromUserBasket(Long userId, Long pizzaId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The basket by the provided user id doesn't exist"));

        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        if (foundBasket.getPizzas().contains(foundPizza)) {
            foundBasket.setTotalPrice(foundBasket.getTotalPrice() - foundPizza.getPrice());
            foundBasket.getPizzas().remove(foundPizza);

            return basketDTOMapper.apply(basketRepository.save(foundBasket));
        }

        throw new ResourceNotFoundException("The provided user doesn't have the pizza to be removed in his basket");
    }
}
