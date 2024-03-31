package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Pizza> pizzaList;

    @Column(name = "total_price")
    private double price;

    @Column(name = "created_at")
    private Date date = java.sql.Date.valueOf(LocalDate.now());
}
