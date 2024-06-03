package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ReviewService {
    ReviewDTO save(Review review);

    ReviewDTO findById(Long reviewId);
    List<ReviewDTO> findAll();
    List<ReviewDTO> findAllByUserId(Long userId);

    ReviewDTO updateById(Long reviewId, Review updatedReview);

    void deleteById(Long reviewId);
    void deleteAllByUserId(Long userId);
}
