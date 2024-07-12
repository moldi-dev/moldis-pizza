package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.request.admin.PizzaCreateAdminRequest;
import org.moldidev.moldispizza.request.admin.PizzaUpdateDetailsAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface PizzaService {
    PizzaDTO save(PizzaCreateAdminRequest request);

    PizzaDTO findById(Long pizzaId);
    PizzaDTO findByName(String name);
    Page<PizzaDTO> findAll(int page, int size);

    PizzaDTO updateById(Long pizzaId, PizzaUpdateDetailsAdminRequest request);

    void deleteById(Long pizzaId);
}
