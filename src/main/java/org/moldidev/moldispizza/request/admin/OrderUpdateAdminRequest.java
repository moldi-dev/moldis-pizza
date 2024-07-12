package org.moldidev.moldispizza.request.admin;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.moldidev.moldispizza.enumeration.OrderStatus;

public record OrderUpdateAdminRequest(
        @DecimalMin(value = "0.0", inclusive = false, message = "The total price must be positive")
        @NotNull(message = "The total price is required")
        Double totalPrice,

        @NotNull(message = "The order status is required")
        @Enumerated(EnumType.STRING)
        OrderStatus status
) {
}
