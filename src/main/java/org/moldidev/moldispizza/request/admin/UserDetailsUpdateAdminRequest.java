package org.moldidev.moldispizza.request.admin;

import jakarta.validation.constraints.*;

public record UserDetailsUpdateAdminRequest(
        @NotNull(message = "The username is required")
        @NotEmpty(message = "The username is required")
        @NotBlank(message = "The username is required")
        String username,

        @NotNull(message = "The first name is required")
        @NotEmpty(message = "The first name is required")
        @NotBlank(message = "The first name is required")
        @Size(max = 50, message = "The first name can contain at most 50 characters")
        String firstName,

        @NotNull(message = "The last name is required")
        @NotEmpty(message = "The last name is required")
        @NotBlank(message = "The last name is required")
        @Size(max = 50, message = "The last name can contain at most 50 characters")
        String lastName,

        @Email(message = "The email must follow the pattern 'email@domain.com'")
        @NotNull(message = "The email is required")
        @NotEmpty(message = "The email is required")
        @NotBlank(message = "The email is required")
        String email,

        @NotNull(message = "The address is required")
        @NotEmpty(message = "The address is required")
        @NotBlank(message = "The address is required")
        String address
) {
}
