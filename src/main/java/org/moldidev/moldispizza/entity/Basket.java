package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany
    private List<Pizza> pizzaList;
}
