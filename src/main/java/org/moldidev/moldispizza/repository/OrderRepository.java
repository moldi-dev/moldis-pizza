package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserUserId(Long userId);
}
