package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "pizzas")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(100) unique not null")
    private String name;

    @Column(name = "ingredients", columnDefinition = "text not null")
    private String ingredients;

    @Column(name = "price", columnDefinition = "double precision not null check(price > 0)")
    private double price;
}
