package org.moldidev.moldispizza.dto;

import org.moldidev.moldispizza.entity.Basket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BasketDTOMapper implements Function<Basket, BasketDTO> {

    UserDTOMapper userDTOMapper = new UserDTOMapper();

    @Override
    public BasketDTO apply(Basket basket) {
        return new BasketDTO(basket.getId(),
                userDTOMapper.apply(basket.getUser()),
                basket.getPizzaList());
    }
}
