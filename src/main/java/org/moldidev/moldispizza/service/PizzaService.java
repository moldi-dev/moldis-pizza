package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface PizzaService {
    PizzaDTO save(Pizza pizza);

    PizzaDTO findById(Long pizzaId);
    PizzaDTO findByName(String name);
    List<PizzaDTO> findAll();

    PizzaDTO updateById(Long pizzaId, Pizza updatedPizza);
    PizzaDTO updateByName(String name, Pizza updatedPizza);

    void deleteById(Long pizzaId);
    void deleteByName(String name);
}
