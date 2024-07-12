package org.moldidev.moldispizza.request.admin;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PizzaUpdateDetailsAdminRequest(
        @NotEmpty(message = "The pizza's name is required")
        @NotNull(message = "The pizza's name is required")
        @NotBlank(message = "The pizza's name is required")
        String name,

        @NotEmpty(message = "At least one image is required")
        @NotNull(message = "At least one image is required")
        List<MultipartFile> images,

        String ingredients,

        @DecimalMin(value = "0.0", inclusive = false, message = "The pizza's price must be positive")
        @NotNull(message = "The price is required")
        Double price
) {
}
