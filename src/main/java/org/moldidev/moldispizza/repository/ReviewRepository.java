package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Long countReviewsByUserUserIdAndPizzaPizzaId(Long userId, Long pizzaId);
    Page<Review> findAllByUserUserId(Long userId, Pageable pageable);
    Page<Review> findAllByPizzaPizzaId(Long pizzaId, Pageable pageable);
}
