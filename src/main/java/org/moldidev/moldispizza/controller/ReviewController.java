package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<HTTPResponse> save(@RequestBody Review review) {
        ReviewDTO result = reviewService.save(review);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Review created successfully")
                        .data(Map.of("reviewsDTOs", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PatchMapping
    public ResponseEntity<HTTPResponse> updateById(@RequestParam("id") Long reviewId, @RequestBody Review updatedReview) {
        ReviewDTO result = reviewService.updateById(reviewId, updatedReview);

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

    @DeleteMapping
    public ResponseEntity<HTTPResponse> deleteById(@RequestParam("id") Long reviewId) {
        reviewService.deleteById(reviewId);

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
