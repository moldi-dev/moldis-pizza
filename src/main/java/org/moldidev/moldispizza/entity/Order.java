package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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

    @OneToOne
    private Basket basket;

    @Column(name = "price")
    private double price;

    @Column(name = "date")
    private Date date = java.sql.Date.valueOf(LocalDate.now());
}
