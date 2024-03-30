package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(100) not null")
    @NonNull
    private String name;

    @Column(name = "ingredients", columnDefinition = "text not null")
    @NonNull
    private String ingredients;

    @Column(name = "price", columnDefinition = "double precision not null check(price > 0)")
    @NonNull
    private double price;
}
