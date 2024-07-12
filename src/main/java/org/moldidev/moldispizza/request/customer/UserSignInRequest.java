package org.moldidev.moldispizza.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserSignInRequest(
        @NotNull(message = "The username is required")
        @NotEmpty(message = "The username is required")
        @NotBlank(message = "The username is required")
        String username,

        @NotNull(message = "The password is required")
        @NotEmpty(message = "The password is required")
        @NotBlank(message = "The password is required")
        String password,

        @NotNull(message = "The remember me is required")
        @NotEmpty(message = "The remember me is required")
        @NotBlank(message = "The remember me is required")
        String rememberMe
) {
}
