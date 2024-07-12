package org.moldidev.moldispizza.request.admin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewUpdateAdminRequest(

        @Min(value = 1, message = "The minimum rating allowed is 1 star")
        @Max(value = 5, message = "The maximum rating allowed is 5 stars")
        @NotNull(message = "The rating is required")
        Integer rating,

        String comment
) {
}
