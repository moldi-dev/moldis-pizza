package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza,Long> {
    Optional<Pizza> findByName(String name);

    @Query(value = "SELECT p.* FROM public.pizzas AS p " +
            "JOIN public.pizzas_images AS pi ON p.pizza_id = pi.pizza_pizza_id " +
            "JOIN public.images AS i ON pi.images_image_id = i.image_id " +
            "WHERE i.image_id = :image_id", nativeQuery = true)
    Optional<Pizza> findByImageId(@Param("image_id") Long imageId);

    @Query(value = "SELECT p.* FROM public.pizzas AS p " +
            "JOIN public.pizzas_images AS pi ON p.pizza_id = pi.pizza_pizza_id " +
            "JOIN public.images AS i ON pi.images_image_id = i.image_id " +
            "WHERE i.url = :image_url", nativeQuery = true)
    Optional<Pizza> findByImageUrl(@Param("image_url") String imageUrl);
}
