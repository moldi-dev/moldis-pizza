package org.moldidev.moldispizza.request.customer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserCreateReviewRequest(
        @Min(value = 1, message = "The minimum rating allowed is 1")
        @Max(value = 5, message = "The maximum rating allowed is 5")
        @NotNull(message = "The rating is required")
        Integer rating,

        String comment
) {
}
