package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.OrderStatus;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.ReviewDTOMapper;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.ReviewRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.moldidev.moldispizza.request.admin.ReviewUpdateAdminRequest;
import org.moldidev.moldispizza.request.customer.UserCreateReviewRequest;
import org.moldidev.moldispizza.service.ReviewService;
import org.moldidev.moldispizza.service.SecurityService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDTOMapper reviewDTOMapper;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final PizzaRepository pizzaRepository;
    private final OrderRepository orderRepository;

    private final ObjectValidator<UserCreateReviewRequest> userCreateRequestValidator;
    private final ObjectValidator<ReviewUpdateAdminRequest> reviewUpdateAdminRequestValidator;

    @Override
    public ReviewDTO findById(Long reviewId) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("The review by the provided id doesn't exist"));

        return reviewDTOMapper.apply(foundReview);
    }

    @Override
    public Page<ReviewDTO> findAll(int page, int size) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size));

        if (reviews.isEmpty()) {
           throw new ResourceNotFoundException("No reviews exist");
        }

        return reviews.map(reviewDTOMapper);
    }

    @Override
    public Page<ReviewDTO> findAllByUserId(Long userId, int page, int size) {
        Page<Review> reviews = reviewRepository.findAllByUserUserId(userId, PageRequest.of(page, size));

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews exist by the provided user id");
        }

        return reviews.map(reviewDTOMapper);
    }

    @Override
    public Page<ReviewDTO> findAllByPizzaId(Long pizzaId, int page, int size) {
        Page<Review> reviews = reviewRepository.findAllByPizzaPizzaId(pizzaId, PageRequest.of(page, size));

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews exist by the provided pizza id");
        }

        return reviews.map(reviewDTOMapper);
    }

    @Override
    public Boolean hasUserReviewedThePizza(Long userId, Long pizzaId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        return reviewRepository.existsReviewByUserUserIdAndPizzaPizzaId(userId, pizzaId);
    }

    @Override
    public ReviewDTO postReviewByUserIdAndPizzaId(Long userId, Long pizzaId, UserCreateReviewRequest request, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        userCreateRequestValidator.validate(request);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        boolean hasUserAlreadyReviewedThePizza = reviewRepository
                .existsReviewByUserUserIdAndPizzaPizzaId(userId, pizzaId);

        boolean hasUserBoughtThePizza = orderRepository.existsByUserUserIdAndPizzasPizzaIdAndStatus(userId, pizzaId, OrderStatus.DELIVERED);

        if (!hasUserBoughtThePizza) {
            throw new ResourceNotFoundException("This user did not buy the pizza yet");
        }

        if (hasUserAlreadyReviewedThePizza) {
            throw new ResourceAlreadyExistsException("This user already reviewed this pizza");
        }

        Review review = new Review();

        review.setPizza(foundPizza);
        review.setUser(foundUser);
        review.setRating(request.rating());
        review.setComment(request.comment());

        return reviewDTOMapper.apply(reviewRepository.save(review));
    }

    @Override
    public ReviewDTO updateById(Long reviewId, ReviewUpdateAdminRequest request) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("The review by the provided id doesn't exist"));

        reviewUpdateAdminRequestValidator.validate(request);

        foundReview.setRating(request.rating());
        foundReview.setComment(request.comment());

        return reviewDTOMapper.apply(reviewRepository.save(foundReview));
    }

    @Override
    public void deleteById(Long reviewId, Authentication connectedUser) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("The review by the provided id doesn't exist"));

        securityService.validateAuthenticatedUser(connectedUser, foundReview.getUser().getUserId());

        reviewRepository.delete(foundReview);
    }

    @Override
    public void delete(Review review) {
        reviewRepository.delete(review);
    }
}
