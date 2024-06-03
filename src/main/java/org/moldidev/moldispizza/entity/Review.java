package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Table(name = "reviews")
@Entity
@Data
public class Review {
    @Column(name = "review_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "rating")
    @Min(value = 1, message = "The minimum rating allowed is 1 star")
    @Max(value = 5, message = "The maximum rating allowed is 5 stars")
    private Integer rating;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizzaId", referencedColumnName = "pizza_id", nullable = false)
    private Pizza pizza;
}
