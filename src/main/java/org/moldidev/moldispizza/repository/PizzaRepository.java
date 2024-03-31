package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findPizzaByName(String name);
    void deletePizzaByName(String name);
}
