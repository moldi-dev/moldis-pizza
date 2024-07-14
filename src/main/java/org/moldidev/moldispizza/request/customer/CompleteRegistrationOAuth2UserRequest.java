package org.moldidev.moldispizza.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompleteRegistrationOAuth2UserRequest(
        @NotNull(message = "The username is required")
        @NotEmpty(message = "The username is required")
        @NotBlank(message = "The username is required")
        String username,

        @NotNull(message = "The first name is required")
        @NotEmpty(message = "The first name is required")
        @NotBlank(message = "The first name is required")
        String firstName,

        @NotNull(message = "The last name is required")
        @NotEmpty(message = "The last name is required")
        @NotBlank(message = "The last name is required")
        String lastName,

        @NotNull(message = "The email is required")
        @NotEmpty(message = "The email is required")
        @NotBlank(message = "The email is required")
        String email,

        @NotNull(message = "The address is required")
        @NotEmpty(message = "The address is required")
        @NotBlank(message = "The address is required")
        String address,

        @NotNull(message = "The password is required")
        @NotEmpty(message = "The password is required")
        @NotBlank(message = "The password is required")
        @Size(min = 8, message = "The password must contain at least 8 characters")
        String password,

        @NotNull(message = "The password confirmation is required")
        @NotEmpty(message = "The password confirmation is required")
        @NotBlank(message = "The password confirmation is required")
        String confirmPassword
) {
}
