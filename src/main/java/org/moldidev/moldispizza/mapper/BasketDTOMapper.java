package org.moldidev.moldispizza.mapper;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketDTOMapper implements Function<Basket, BasketDTO> {

    private final UserDTOMapper userDTOMapper;
    private final PizzaDTOMapper pizzaDTOMapper;

    @Override
    public BasketDTO apply(Basket basket) {
        return new BasketDTO(
                basket.getBasketId(),
                userDTOMapper.apply(basket.getUser()),
                basket.getTotalPrice(),
                basket.getPizzas()
                        .stream()
                        .map(pizzaDTOMapper::apply)
                        .collect(Collectors.toList())
        );
    }
}
