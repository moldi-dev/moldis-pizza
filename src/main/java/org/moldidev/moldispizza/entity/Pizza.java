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
public class Pizza {

    @Column(name = "pizza_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizzaId;

    @Column(name = "name", unique = true)
    @NotEmpty(message = "The pizza's name can't be empty")
    @NotNull(message = "The pizza's name can't be null")
    @NotBlank(message = "The pizza's name can't be blank")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Image> images;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "The pizza's price must be positive")
    private Double price;
}
