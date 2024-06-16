package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.BasketDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.service.BasketService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasketServiceImplementation implements BasketService {

    private final BasketRepository basketRepository;
    private final BasketDTOMapper basketDTOMapper;
    private final ObjectValidator<Basket> objectValidator;

    @Override
    public BasketDTO save(Basket basket) {
        objectValidator.validate(basket);
        return basketDTOMapper.apply(basketRepository.save(basket));
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
    public Page<BasketDTO> findAll(int page, int size) {
        Page<Basket> baskets = basketRepository.findAll(PageRequest.of(page, size));

        if (baskets.isEmpty()) {
            throw new ResourceNotFoundException("No baskets found");
        }

        return baskets.map(basketDTOMapper);
    }

    @Override
    public BasketDTO updateById(Long basketId, Basket updatedBasket) {
        Basket foundBasket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by id " + basketId));

        objectValidator.validate(updatedBasket);

        foundBasket.setPizzas(updatedBasket.getPizzas());
        foundBasket.setUser(updatedBasket.getUser());
        foundBasket.setTotalPrice(updatedBasket.getTotalPrice());

        return basketDTOMapper.apply(basketRepository.save(foundBasket));
    }

    @Override
    public void deleteById(Long basketId) {
        Basket foundBasket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found by id " + basketId));

        basketRepository.delete(foundBasket);
    }
}
