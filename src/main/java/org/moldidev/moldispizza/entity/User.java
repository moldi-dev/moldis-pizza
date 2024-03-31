package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", columnDefinition = "varchar(50) unique not null")
    private String username;

    @Column(name = "password", columnDefinition = "text not null")
    private String password;

    @Column(name = "first_name", columnDefinition = "varchar(100) not null")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(100) not null")
    private String lastName;

    @Column(name = "email", columnDefinition = "varchar(200) not null")
    private String email;

    @Column(name = "address", columnDefinition = "text not null")
    private String address;

    @Column(name = "role", columnDefinition = "varchar(20) default 'CUSTOMER'")
    private String role = "CUSTOMER";
}
