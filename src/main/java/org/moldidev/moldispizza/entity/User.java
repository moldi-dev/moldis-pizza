package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.moldidev.moldispizza.enumeration.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
@Data
public class User extends Auditable implements UserDetails {

    @Column(name = "user_id", updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", unique = true, updatable = false)
    @NotEmpty(message = "The username is required")
    @NotNull(message = "The username is required")
    @NotBlank(message = "The username is required")
    @Size(min = 10, max = 100, message = "The username must contain at least 10 characters and at most 100 characters")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "imageId", referencedColumnName = "image_id")
    private Image image;

    @Column(name = "password")
    @NotNull(message = "The password is required")
    @NotEmpty(message = "The password is required")
    @NotBlank(message = "The password is required")
    private String password;

    @Column(name = "first_name")
    @NotNull(message = "The first name is required")
    @NotEmpty(message = "The first name is required")
    @NotBlank(message = "The first name is required")
    @Size(max = 50, message = "The first name can contain at most 50 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "The last name is required")
    @NotEmpty(message = "The last name is required")
    @NotBlank(message = "The last name is required")
    @Size(max = 50, message = "The last name can contain at most 50 characters")
    private String lastName;

    @Column(name = "email", unique = true)
    @Email(message = "The email must follow the pattern 'email@domain.com'")
    @NotNull(message = "The email is required")
    @NotEmpty(message = "The email is required")
    @NotBlank(message = "The email is required")
    private String email;

    @Column(name = "address")
    @NotNull(message = "The address is required")
    @NotEmpty(message = "The address is required")
    @NotBlank(message = "The address is required")
    private String address;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
