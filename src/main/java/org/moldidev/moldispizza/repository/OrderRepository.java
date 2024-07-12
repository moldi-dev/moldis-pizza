package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByUserUserId(Long userId, Pageable pageable);
    Page<Order> findAllByPizzasPizzaId(Long pizzaId, Pageable pageable);
    Boolean existsByUserUserIdAndPizzasPizzaIdAndStatus(Long userId, Long pizzaId, OrderStatus status);
}
