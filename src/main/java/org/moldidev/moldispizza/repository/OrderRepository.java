package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserUserId(Long userId, Pageable pageable);

    Boolean existsByUserUserIdAndPizzasPizzaId(Long userId, Long pizzaId);
}
