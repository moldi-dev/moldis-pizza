package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.exception.InvalidInputException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.ReviewDTOMapper;
import org.moldidev.moldispizza.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewDTOMapper reviewDTOMapper;

    public ReviewServiceImplementation(ReviewRepository reviewRepository, ReviewDTOMapper reviewDTOMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewDTOMapper = reviewDTOMapper;
    }

    @Override
    public ReviewDTO save(Review review) {
        checkIfReviewIsValid(review);

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
    public List<ReviewDTO> findAll() {
        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) {
           throw new ResourceNotFoundException("No reviews found");
        }

        return reviews
                .stream()
                .map(reviewDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> findAllByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserUserId(userId);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found by user id " + userId);
        }

        return reviews
                .stream()
                .map(reviewDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> findAllByPizzaId(Long pizzaId) {
        List<Review> reviews = reviewRepository.findAllByPizzaPizzaId(pizzaId);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found by pizza id " + pizzaId);
        }

        return reviews
                .stream()
                .map(reviewDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateById(Long reviewId, Review updatedReview) {
        checkIfReviewIsValid(updatedReview);

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("No review found by id " + reviewId));

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

    @Override
    public void deleteAllByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserUserId(userId);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found by user id " + userId);
        }

        reviewRepository.deleteAll(reviews);
    }

    private void checkIfReviewIsValid(Review review) {
        if (review.getUser() == null) {
            throw new InvalidInputException("The user can't be null");
        }

        else if (review.getRating() == null) {
            throw new InvalidInputException("The rating can't be null");
        }

        else if (review.getRating() < 1) {
            throw new InvalidInputException("The minimum rating allowed is 1 star");
        }

        else if (review.getRating() > 5) {
            throw new InvalidInputException("The maximum rating allowed is 5 stars");
        }

        else if (review.getPizza() == null) {
            throw new InvalidInputException("The pizza can't be null");
        }
    }
}
