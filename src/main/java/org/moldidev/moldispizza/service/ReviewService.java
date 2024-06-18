package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ReviewService {
    ReviewDTO save(Review review);

    ReviewDTO findById(Long reviewId);
    Page<ReviewDTO> findAll(int page, int size);
    Page<ReviewDTO> findAllByUserId(Long userId, int page, int size);
    Page<ReviewDTO> findAllByPizzaId(Long pizzaId, int page, int size);

    ReviewDTO updateById(Long reviewId, Review updatedReview, Authentication authentication);

    void deleteById(Long reviewId, Authentication authentication);
}
