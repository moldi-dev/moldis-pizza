package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.request.admin.ReviewUpdateAdminRequest;
import org.moldidev.moldispizza.request.customer.UserCreateReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ReviewService {
    ReviewDTO findById(Long reviewId);
    Page<ReviewDTO> findAll(int page, int size);
    Page<ReviewDTO> findAllByUserId(Long userId, int page, int size);
    Page<ReviewDTO> findAllByPizzaId(Long pizzaId, int page, int size);

    Boolean hasUserReviewedThePizza(Long userId, Long pizzaId, Authentication connectedUser);

    ReviewDTO postReviewByUserIdAndPizzaId(Long userId, Long pizzaId, UserCreateReviewRequest request, Authentication connectedUser);

    ReviewDTO updateById(Long reviewId, ReviewUpdateAdminRequest request);

    void deleteById(Long reviewId, Authentication connectedUser);
    void delete(Review review);
}
