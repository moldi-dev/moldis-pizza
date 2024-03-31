package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Query("SELECT basket FROM Basket basket WHERE basket.user.id = :id")
    Optional<Basket> getBasketByUserId(@Param("id") Long id);

    @Query("SELECT basket FROM Basket basket WHERE basket.user.username = :username")
    Optional<Basket> getBasketByUsername(@Param("username") String username);

    @Query("DELETE FROM Basket basket WHERE basket.user.id = :id")
    void deleteBasketByUserId(@Param("id") Long id);

    @Query("DELETE FROM Basket basket WHERE basket.user.username = :username")
    void deleteBasketByUsername(@Param("username") String username);

    @Query("SELECT SUM(pizza.price) FROM Basket basket JOIN basket.pizzaList pizza WHERE basket.user.id = :id")
    Optional<Double> getBasketTotalPriceByUserId(@Param("id") Long id);

    @Query("SELECT SUM(pizza.price) FROM Basket basket JOIN basket.pizzaList pizza WHERE basket.user.username = :username")
    Optional<Double> getBasketTotalPriceByUsername(@Param("username") String username);
}
