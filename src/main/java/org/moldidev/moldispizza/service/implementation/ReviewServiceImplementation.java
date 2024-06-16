package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.ReviewDTOMapper;
import org.moldidev.moldispizza.repository.ReviewRepository;
import org.moldidev.moldispizza.service.ReviewService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDTOMapper reviewDTOMapper;
    private final ObjectValidator<Review> objectValidator;

    @Override
    public ReviewDTO save(Review review) {
        objectValidator.validate(review);

        boolean hasUserAlreadyReviewedThePizza = reviewRepository
                .countReviewsByUserUserIdAndPizzaPizzaId(review.getUser().getUserId(), review.getPizza().getPizzaId()) > 0;

        if (hasUserAlreadyReviewedThePizza) {
            throw new ResourceAlreadyExistsException("User " + review.getUser().getUsername() + " already reviewed the pizza " + review.getPizza().getName());
        }

        return reviewDTOMapper.apply(reviewRepository.save(review));
    }

    @Override
    public ReviewDTO findById(Long reviewId) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found by id " + reviewId));

        return reviewDTOMapper.apply(foundReview);
    }

    @Override
    public Page<ReviewDTO> findAll(int page, int size) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size));

        if (reviews.isEmpty()) {
           throw new ResourceNotFoundException("No reviews found");
        }

        return reviews.map(reviewDTOMapper);
    }

    @Override
    public Page<ReviewDTO> findAllByUserId(Long userId, int page, int size) {
        Page<Review> reviews = reviewRepository.findAllByUserUserId(userId, PageRequest.of(page, size));

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found by user id " + userId);
        }

        return reviews.map(reviewDTOMapper);
    }

    @Override
    public Page<ReviewDTO> findAllByPizzaId(Long pizzaId, int page, int size) {
        Page<Review> reviews = reviewRepository.findAllByPizzaPizzaId(pizzaId, PageRequest.of(page, size));

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found by pizza id " + pizzaId);
        }

        return reviews.map(reviewDTOMapper);
    }

    @Override
    public ReviewDTO updateById(Long reviewId, Review updatedReview) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("No review found by id " + reviewId));

        objectValidator.validate(updatedReview);

        foundReview.setPizza(updatedReview.getPizza());
        foundReview.setUser(updatedReview.getUser());
        foundReview.setRating(updatedReview.getRating());
        foundReview.setComment(updatedReview.getComment());

        return reviewDTOMapper.apply(reviewRepository.save(foundReview));
    }

    @Override
    public void deleteById(Long reviewId) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("No review found by id " + reviewId));

        reviewRepository.delete(foundReview);
    }
}
