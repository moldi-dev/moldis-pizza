package org.moldidev.moldispizza.request.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserResetPasswordEmailRequest(
        @Email(message = "The email must follow the pattern 'email@domain.com'")
        @NotNull(message = "The email is required")
        @NotEmpty(message = "The email is required")
        @NotBlank(message = "The email is required")
        String email
) {
}
