package org.moldidev.moldispizza.request.customer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserActivateAccountRequest(
        @NotNull(message = "The verification code is required")
        @NotEmpty(message = "The verification code is required")
        String verificationCode
) {
}
