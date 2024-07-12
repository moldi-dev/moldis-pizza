package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.request.admin.ReviewUpdateAdminRequest;
import org.moldidev.moldispizza.request.customer.UserCreateReviewRequest;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<HTTPResponse> findAll(@RequestParam("page") Optional<Integer> page, Optional<Integer> size) {
        Page<ReviewDTO> result = reviewService.findAll(page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("reviewsDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/user-id={user_id}")
    public ResponseEntity<HTTPResponse> findAllByUserId(@PathVariable("user_id") Long userId, @RequestParam("page") Optional<Integer> page, Optional<Integer> size) {
        Page<ReviewDTO> result = reviewService.findAllByUserId(userId, page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("reviewsDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/pizza-id={pizza_id}")
    public ResponseEntity<HTTPResponse> findAllByPizzaId(@PathVariable("pizza_id") Long pizzaId, Optional<Integer> page, Optional<Integer> size) {
        Page<ReviewDTO> result = reviewService.findAllByPizzaId(pizzaId, page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("reviewsDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/id={id}")
    public ResponseEntity<HTTPResponse> findById(@PathVariable("id") Long reviewId) {
        ReviewDTO result = reviewService.findById(reviewId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("reviewDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/exists/user-id={userId}/pizza-id={pizzaId}")
    public ResponseEntity<HTTPResponse> hasUserReviewedThePizza(@PathVariable("userId") Long userId, @PathVariable("pizzaId") Long pizzaId, Authentication connectedUser) {
        Boolean result = reviewService.hasUserReviewedThePizza(userId, pizzaId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("answer", result))
                        .build()
        );
    }

    @PostMapping("/user-id={userId}/pizza-id={pizzaId}")
    public ResponseEntity<HTTPResponse> postReviewByUserIdAndPizzaId(@PathVariable Long userId, @PathVariable Long pizzaId, @RequestBody UserCreateReviewRequest request, Authentication connectedUser) {
        ReviewDTO result = reviewService.postReviewByUserIdAndPizzaId(userId, pizzaId, request, connectedUser);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Review added successfully")
                        .data(Map.of("reviewDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PatchMapping("/admin/id={id}")
    public ResponseEntity<HTTPResponse> updateByIdAdminRequest(@PathVariable("id") Long reviewId, @RequestBody ReviewUpdateAdminRequest request) {
        ReviewDTO result = reviewService.updateById(reviewId, request);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Review updated successfully")
                        .data(Map.of("reviewDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<HTTPResponse> deleteById(@PathVariable("id") Long reviewId, Authentication connectedUser) {
        reviewService.deleteById(reviewId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Review deleted successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
