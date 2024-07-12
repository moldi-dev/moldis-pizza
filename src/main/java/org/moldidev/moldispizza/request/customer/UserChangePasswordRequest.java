package org.moldidev.moldispizza.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(
        @NotNull(message = "The password is required")
        @NotEmpty(message = "The password is required")
        @NotBlank(message = "The password is required")
        String currentPassword,

        @NotNull(message = "The new password is required")
        @NotEmpty(message = "The new password is required")
        @NotBlank(message = "The new password is required")
        @Size(min = 8, message = "The new password must contain at least 8 characters")
        String newPassword,

        @NotNull(message = "The new password confirmation is required")
        @NotEmpty(message = "The new password confirmation is required")
        @NotBlank(message = "The new password confirmation is required")
        String confirmNewPassword
) {
}
