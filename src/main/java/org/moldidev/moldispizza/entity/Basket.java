package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Table(name = "baskets")
@Entity
@Data
public class Basket extends Auditable {

    @Column(name = "basket_id", updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    @NotNull(message = "The user is required")
    private User user;

    @Column(name = "total_price")
    @DecimalMin(value = "0.0", message = "The total price must be positive")
    @NotNull(message = "The total price is required")
    private Double totalPrice;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Pizza> pizzas;
}
