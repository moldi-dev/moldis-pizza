package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Table(name = "baskets")
@Entity
@Data
public class Basket {

    @Column(name = "basket_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price")
    @DecimalMin(value = "0.0", message = "The total price must be positive")
    private Double totalPrice;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull(message = "The pizza list can't be null")
    private List<Pizza> pizzas;

    public void addPizza(@NotNull Pizza pizza) {
        this.pizzas.add(pizza);
    }

    public void removePizza(@NotNull Pizza pizza) {
        this.pizzas.remove(pizza);
    }
}
