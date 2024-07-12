package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.BasketDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface BasketService {
    BasketDTO findById(Long basketId);
    BasketDTO findByUserId(Long userId, Authentication connectedUser);
    Page<BasketDTO> findAll(int page, int size);

    BasketDTO addPizzaToUserBasket(Long userId, Long pizzaId, Authentication connectedUser);
    BasketDTO removePizzaFromUserBasket(Long userId, Long pizzaId, Authentication connectedUser);
}
