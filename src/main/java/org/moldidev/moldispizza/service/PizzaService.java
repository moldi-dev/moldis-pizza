package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface PizzaService {
    PizzaDTO save(Pizza pizza);

    PizzaDTO findById(Long pizzaId);
    PizzaDTO findByName(String name);
    Page<PizzaDTO> findAll(int page, int size);

    PizzaDTO updateById(Long pizzaId, Pizza updatedPizza);

    PizzaDTO addImage(Long pizzaId, Long imageId);
    PizzaDTO removeImage(Long pizzaId, Long imageId);

    void deleteById(Long pizzaId);
}
