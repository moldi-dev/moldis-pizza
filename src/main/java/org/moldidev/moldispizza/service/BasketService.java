package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface BasketService {
    BasketDTO save(Basket basket);

    BasketDTO addPizzaToUserBasketByPizzaIdAndUserId(Long pizzaId, Long userId);

    BasketDTO findById(Long basketId);
    BasketDTO findByUserId(Long userId);
    List<BasketDTO> findAll();

    BasketDTO updateById(Long basketId, Basket updatedBasket);

    BasketDTO removePizzaFromUserBasketByPizzaIdAndUserId(Long pizzaId, Long userId);

    void deleteById(Long basketId);
    void deleteByUserId(Long userId);
}
