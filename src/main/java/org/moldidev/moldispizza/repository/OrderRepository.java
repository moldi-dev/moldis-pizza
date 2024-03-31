package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT order FROM Order order JOIN order.user WHERE order.user.id = :userId")
    List<Order> getAllOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT order FROM Order order JOIN order.user WHERE order.user.username = :username")
    List<Order> getAllOrdersByUsername(@Param("username") String username);
}
