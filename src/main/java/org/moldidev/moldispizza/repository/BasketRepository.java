package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByUserUserId(Long userId);
}
