package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Long countReviewsByUserUserIdAndPizzaPizzaId(Long userId, Long pizzaId);
    List<Review> findAllByUserUserId(Long userId);
    List<Review> findAllByPizzaPizzaId(Long pizzaId);
}
