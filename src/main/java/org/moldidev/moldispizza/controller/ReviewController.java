package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ReviewDTO>> findAll() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/find/id={review_id}")
    public ResponseEntity<ReviewDTO> findById(@PathVariable("review_id") Long reviewId) {
        return ResponseEntity.ok(reviewService.findById(reviewId));
    }

    @GetMapping("/find-all/user-id={user_id}")
    public ResponseEntity<List<ReviewDTO>> findAllByUserId(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(reviewService.findAllByUserId(userId));
    }

    @PostMapping("/save")
    public ResponseEntity<ReviewDTO> save(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.save(review));
    }

    @PostMapping("/update/id={review_id}")
    public ResponseEntity<ReviewDTO> updateById(@PathVariable("review_id") Long reviewId, @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.updateById(reviewId, review));
    }

    @DeleteMapping("/delete/id={review_id}")
    public ResponseEntity<Void> deleteById(@PathVariable("review_id") Long reviewId) {
        reviewService.deleteById(reviewId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-all/user-id={user_id}")
    public ResponseEntity<Void> deleteAllByUserId(@PathVariable("user_id") Long userId) {
        reviewService.deleteAllByUserId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
