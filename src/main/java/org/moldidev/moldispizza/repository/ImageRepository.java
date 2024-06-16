package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String name);

    @Query(value = "SELECT i.* FROM public.images AS i " +
            "JOIN public.users AS u ON i.image_id = u.image_id " +
            "WHERE u.user_id = :user_id", nativeQuery = true)
    Optional<Image> findByUserId(@Param("user_id") Long userId);

    Page<Image> findAllByType(String type, Pageable pageable);

    @Query(value = "SELECT i.* FROM public.images AS i " +
            "JOIN public.pizzas_images AS pi ON i.image_id = pi.images_image_id " +
            "JOIN public.pizzas AS p ON p.pizza_id = pi.pizza_pizza_id " +
            "WHERE p.pizza_id = :pizza_id", nativeQuery = true)
    List<Image> findAllByPizzaId(@Param("pizza_id") Long pizzaId);
}
