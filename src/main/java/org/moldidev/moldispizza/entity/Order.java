package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.moldidev.moldispizza.enumeration.OrderStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Table(name = "orders")
@Entity
@Data
public class Order {

    @Column(name = "order_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull(message = "The pizza list can't be null")
    @NotEmpty(message = "The pizza list can't be empty")
    private List<Pizza> pizzas;

    @Column(name = "total_price")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be positive")
    private Double totalPrice;

    @Column(name = "created_at")
    private Date createdAt = java.sql.Date.valueOf(LocalDate.now());

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
