package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.moldidev.moldispizza.enumeration.OrderStatus;

import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Data
public class Order extends Auditable {

    @Column(name = "order_id", updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    @NotNull(message = "The user is required")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull(message = "The pizza list is required")
    @NotEmpty(message = "The pizza list is required")
    private List<Pizza> pizzas;

    @Column(name = "total_price")
    @DecimalMin(value = "0.0", inclusive = false, message = "The total price must be positive")
    @NotNull(message = "The total price is required")
    private Double totalPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
