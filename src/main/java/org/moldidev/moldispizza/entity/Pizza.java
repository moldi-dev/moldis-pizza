package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Table(name = "pizzas")
@Entity
@Data
public class Pizza extends Auditable {

    @Column(name = "pizza_id", updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizzaId;

    @Column(name = "name", unique = true)
    @NotEmpty(message = "The pizza's name is required")
    @NotNull(message = "The pizza's name is required")
    @NotBlank(message = "The pizza's name is required")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "The pizza's price must be positive")
    @NotNull(message = "The total price is required")
    private Double price;
}
