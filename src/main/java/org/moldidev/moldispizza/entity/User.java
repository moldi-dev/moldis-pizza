package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.moldidev.moldispizza.enumeration.Role;

@Table(name = "users")
@Entity
@Data
public class User {
    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", unique = true)
    @NotEmpty(message = "The username can't be empty")
    @NotNull(message = "The username can't be null")
    @NotBlank(message = "The username can't be blank")
    @Size(min = 10, max = 100, message = "The username must contain at least 10 characters and at most 100 characters")
    private String username;

    @ManyToOne
    @JoinColumn(name = "imageId", referencedColumnName = "image_id")
    private Image image;

    @Column(name = "password")
    @NotNull(message = "The password can't be null")
    @NotEmpty(message = "The password can't be empty")
    @NotBlank(message = "The password can't be blank")
    private String password;

    @Column(name = "first_name")
    @NotNull(message = "The first name can't be null")
    @NotEmpty(message = "The first name can't be empty")
    @NotBlank(message = "The first name can't be blank")
    @Size(max = 50, message = "The first name can contain at most 50 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "The last name can't be null")
    @NotEmpty(message = "The last name can't be empty")
    @NotBlank(message = "The last name can't be blank")
    @Size(max = 50, message = "The last name can contain at most 50 characters")
    private String lastName;

    @Column(name = "email")
    @Email(message = "The email must follow the pattern 'email@domain.com'")
    private String email;

    @Column(name = "address")
    @NotNull(message = "The address can't be null")
    @NotEmpty(message = "The address can't be empty")
    @NotBlank(message = "The address can't be blank")
    private String address;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_locked")
    private Boolean isLocked;
}
